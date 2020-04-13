package com.kirik88.lannister.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kirik88.lannister.model.network.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

class WebApiImpl : WebApi {

    override suspend fun loadCharacters(page: Long, pageSize: Long) : List<Character> {
        return withContext(Dispatchers.IO) {
            val genericType = genericType<List<Character>>()
            loadAndParse<List<Character>>("$CHARACTERS_URL?page=$page&pageSize=$pageSize", genericType)
        }
    }

    override suspend fun loadCharacter(id: Long) : Character {
        return withContext(Dispatchers.IO) {
            loadAndParse<Character>("$CHARACTERS_URL$id/")
        }
    }

    private inline fun <reified T: Any> loadAndParse(url: String) : T {
        return loadAndParse(url, T::class.java)
    }

    private inline fun <reified T: Any> loadAndParse(url: String, cls: Type) : T {
        return (URL(url).openConnection() as HttpURLConnection).let { connection ->
            try {
                val reader = InputStreamReader(connection.inputStream)
                Gson().fromJson(reader, cls)
            } finally {
                connection.disconnect()
            }
        }
    }

    private inline fun <reified T> genericType() = object: TypeToken<T>() {}.type

    companion object {
        private const val HOST_URL = "https://anapioficeandfire.com/api/"
        const val CHARACTERS_URL = "${HOST_URL}characters/"
        const val HOUSES_URL = "${HOST_URL}houses/"
        const val BOOKS_URL = "${HOST_URL}books/"
    }
}