package com.quizle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quizle.data.utils.Constant.QUIZ_RESULT_TABLE_NAME


@Entity(tableName = QUIZ_RESULT_TABLE_NAME)
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topicId: String,
    val createdAt: Long,
    val totalQuestions: Int,
    val correctAnswersCount: Int,
    val inCorrectAnswersCount: Int,
    val topicTitleEn: String,
    val topicTitleAr: String,
    val topicSubTitleEn: String,
    val topicSubTitleAr: String,
    val topicTags: List<String>,
)
