package com.quizle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quizle.data.util.Constant.QUIZ_QUESTION_TABLE_NAME

@Entity(tableName = QUIZ_QUESTION_TABLE_NAME)
data class QuizQuestionEntity(
    @PrimaryKey
    val id: String,
    val ownersIds: List<String>,
    val masterOwnerId: String,
    val questionText: String,
    val correctAnswer: String,
    val inCorrectAnswers: List<String>,
    val imageUrl: String?,
    val topicId: String,
    val createdAt: Long,
    val updatedAt: Long,
    val reportCount: Int,
    val level: String,
    val tags: List<String>,
    val explanation: String
)


