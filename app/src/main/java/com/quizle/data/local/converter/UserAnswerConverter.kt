package com.quizle.data.local.converter

import androidx.room.TypeConverter
import com.quizle.domain.module.UserAnswer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json



object UserAnswerConverter {

    @TypeConverter
    fun toUserAnswerListString(list: List<UserAnswer>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromUserAnswerStringList(string: String): List<UserAnswer> {
        return Json.decodeFromString(string)
    }
}