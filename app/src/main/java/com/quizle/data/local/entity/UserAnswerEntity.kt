package com.quizle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quizle.data.utils.Constant.USER_ANSWER_TABLE_NAME


@Entity(tableName = USER_ANSWER_TABLE_NAME)
data class UserAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questionId: String,
    val selectedOption: String,
    val topicId: String
)
