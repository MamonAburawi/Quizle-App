package com.quizle.data.local.converter

import androidx.room.TypeConverter
import com.quizle.data.local.entity.UserEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object SettingsConverter {

    @TypeConverter
    fun toSettingsString(settings: UserEntity.Settings): String {
        return Json.encodeToString(settings)
    }

    @TypeConverter
    fun fromStringSettings(string: String): UserEntity.Settings {
        return Json.decodeFromString(string)
    }

}