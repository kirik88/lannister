package com.kirik88.lannister.network

import com.kirik88.lannister.model.network.Character

interface WebApi {

    suspend fun loadCharacters(page: Long, pageSize: Long) : List<Character>

    suspend fun loadCharacter(id: Long) : Character
}