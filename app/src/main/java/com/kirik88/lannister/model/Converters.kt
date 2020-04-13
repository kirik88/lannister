package com.kirik88.lannister.model

import com.kirik88.lannister.model.db.Character
import com.kirik88.lannister.network.WebApiImpl

fun com.kirik88.lannister.model.network.Character.toDb(displayInList: Boolean) : Character {
    return Character(
        id = url.extractCharacterId()!!,
        name = name,
        gender = gender,
        culture = culture,
        born = born,
        died = died,
        titles = titles,
        aliases = aliases,
        father = father.extractCharacterId(),
        mother = mother.extractCharacterId(),
        spouse = spouse.extractCharacterId(),
        allegiances = allegiances.extractHousesIds(),
        books = books.extractBooksIds(),
        povBooks = povBooks.extractBooksIds(),
        tvSeries = tvSeries,
        playedBy = playedBy,
        displayInList = displayInList
    )
}

fun String.extractCharacterId() : Long? {
    if (isNullOrEmpty())
        return null

    val url = this.replace("https://www.", "https://")
    val regex = "${WebApiImpl.CHARACTERS_URL}(\\d+)".toRegex()
    return regex.find(url)!!.groupValues[1].toLong()
}

fun String.extractHouseId() : Long? {
    if (isNullOrEmpty())
        return null

    val url = this.replace("https://www.", "https://")
    val regex = "${WebApiImpl.HOUSES_URL}(\\d+)".toRegex()
    return regex.find(url)!!.groupValues[1].toLong()
}

fun String.extractBookId() : Long? {
    if (isNullOrEmpty())
        return null

    val url = this.replace("https://www.", "https://")
    val regex = "${WebApiImpl.BOOKS_URL}(\\d+)".toRegex()
    return regex.find(url)!!.groupValues[1].toLong()
}

fun List<String>.extractHousesIds() : List<Long> {
    val ids = mutableListOf<Long>()
    for (house in this) {
        house.extractHouseId()?.let {
            ids.add(it)
        }
    }
    return ids
}

fun List<String>.extractBooksIds() : List<Long> {
    val ids = mutableListOf<Long>()
    for (house in this) {
        house.extractBookId()?.let {
            ids.add(it)
        }
    }
    return ids
}