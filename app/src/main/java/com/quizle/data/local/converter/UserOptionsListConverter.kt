package com.quizle.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object UserOptionsListConverter {

    @TypeConverter
    fun toListString(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStringList(string: String): List<String> {
        return Json.decodeFromString(string)
    }
}