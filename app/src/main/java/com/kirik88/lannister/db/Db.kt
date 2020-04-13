package com.kirik88.lannister.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kirik88.lannister.model.db.Character

data class FullCharacter(
    @Embedded val character: Character,
    val fatherName: String?,
    val motherName: String?,
    val spouseName: String?
)

@Dao
interface CharacterDao {

    @Transaction
    @Query("""
        SELECT MAX(id) FROM Character
        WHERE (displayInList = 1)
        ORDER BY id DESC
    """)
    fun getLastId(): LiveData<Long?>

    @Transaction
    @Query("""
        SELECT * FROM Character
        WHERE (:startId <= id AND id <= :endId) AND (displayInList = 1)
        ORDER BY id
    """)
    fun getSome(startId: Long, endId: Long): LiveData<List<Character>>

    @Transaction
    @Query("""
        SELECT D.*, 
            F.name AS [fatherName],
            M.name AS [motherName],
            S.name AS [spouseName] 
        FROM Character D
        LEFT OUTER JOIN Character F ON (F.id = D.father)
        LEFT OUTER JOIN Character M ON (M.id = D.mother)
        LEFT OUTER JOIN Character S ON (S.id = D.spouse)
        WHERE D.id = :id
    """)
    fun getById(id: Long): LiveData<FullCharacter?>

    @Query("""
        SELECT * FROM Character
        WHERE id = :id
    """)
    fun loadById(id: Long): Character?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(character: Character) : Long
}

@Database(
    entities = [Character::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class Db : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}