package com.quizle.data.local.converter

import androidx.room.TypeConverter
import com.quizle.data.local.entity.UserEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object TokenConverter {

    @TypeConverter
    fun toTokenString(token: UserEntity.Token): String {
        return Json.encodeToString(token)
    }

    @TypeConverter
    fun fromStringToken(string: String): UserEntity.Token {
        return Json.decodeFromString(string)
    }

}