package com.kirik88.lannister.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun toListString(value: String?): List<String> {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListString(list: List<String?>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toListLong(value: String?): List<Long> {
        val listType: Type = object : TypeToken<ArrayList<Long?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListLong(list: List<Long?>?): String {
        return Gson().toJson(list)
    }
}