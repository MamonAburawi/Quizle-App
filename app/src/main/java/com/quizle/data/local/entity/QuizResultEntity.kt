package com.quizle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quizle.data.utils.Constant.QUIZ_RESULT_TABLE_NAME
import com.quizle.data.utils.Constant.USER_ANSWER_TABLE_NAME
import com.quizle.domain.module.UserAnswer
import kotlinx.serialization.Serializable


@Entity(tableName = QUIZ_RESULT_TABLE_NAME)
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val topicId: String,
    val createdAt: Long,
    val totalQuestions: Int,
    val correctAnswersCount: Int,
    val inCorrectAnswersCount: Int,
    val questionsIds: List<String> = emptyList(),
    val answerIds: List<String> = emptyList(),
    val topicTitleEn: String,
    val topicTitleAr: String,
    val topicSubTitleEn: String,
    val topicSubTitleAr: String,
    val topicTags: List<String>,
)
