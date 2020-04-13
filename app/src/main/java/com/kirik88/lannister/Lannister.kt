package com.kirik88.lannister

import android.app.Application
import androidx.room.Room
import com.kirik88.lannister.db.Db
import com.kirik88.lannister.di.DI
import com.kirik88.lannister.network.WebApi
import com.kirik88.lannister.network.WebApiImpl
import com.kirik88.lannister.repository.CharactersRepository

class Lannister : Application() {

    override fun onCreate() {
        super.onCreate()

        DI.set(this)

        DI.set(
            //Room.inMemoryDatabaseBuilder(this, Db::class.java).build()
            Room.databaseBuilder(this, Db::class.java, "lannister").build()
        )
        DI.set<WebApi>(WebApiImpl())

        DI.set(CharactersRepository())
    }
}