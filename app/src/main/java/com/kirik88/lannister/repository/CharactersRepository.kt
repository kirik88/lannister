package com.kirik88.lannister.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.withTransaction
import com.kirik88.lannister.db.Db
import com.kirik88.lannister.db.FullCharacter
import com.kirik88.lannister.di.DI
import com.kirik88.lannister.model.db.Character
import com.kirik88.lannister.model.toDb
import com.kirik88.lannister.network.WebApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharactersRepository {

    private val db = DI.get<Db>()
    private val webApi = DI.get<WebApi>()

    fun getLastCharactersPage() : LiveData<Long> {
        return Transformations.map(db.characterDao().getLastId()) { lastId ->
            if (lastId != null)
                (lastId - 1) / PAGE_SIZE + 1
            else
                1
        }
    }

    fun getCharacters(page: Long) : LiveData<List<Character>> {
        val startId = 1L
        val endId = startId + PAGE_SIZE * page - 1L
        return db.characterDao().getSome(startId, endId)
    }

    suspend fun refreshCharacters(page: Long) {
        withContext(Dispatchers.IO) {
            webApi.loadCharacters(page, PAGE_SIZE).let { list ->
                db.withTransaction {
                    for (character in list)
                        db.characterDao().save(character.toDb(true))
                }
            }
        }
    }

    fun getCharacter(id: Long) : LiveData<FullCharacter?> {
        return db.characterDao().getById(id)
    }

    suspend fun refreshCharacter(id: Long) {
        withContext(Dispatchers.IO) {
            db.withTransaction {
                webApi.loadCharacter(id).let {
                    val displayInList = db.characterDao().loadById(id)?.displayInList ?: false
                    it.toDb(displayInList).apply {
                        db.characterDao().save(this)
                    }
                }
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20L
    }
}