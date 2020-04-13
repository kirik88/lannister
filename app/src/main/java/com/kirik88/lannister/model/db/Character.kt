package com.kirik88.lannister.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey val id: Long,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    val father: Long?,
    val mother: Long?,
    val spouse: Long?,
    val allegiances: List<Long>,
    val books: List<Long>,
    val povBooks: List<Long>,
    val tvSeries: List<String>,
    val playedBy: List<String>,
    val displayInList: Boolean
)