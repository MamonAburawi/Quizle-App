package com.quizle.domain.module

import kotlinx.serialization.Serializable


@Serializable
data class QuizResult(
    val id: Int = 0,
    val topicId: String = "",
    val createdAt: Long = 0L,
    val topicTitleEn: String = "",
    val topicTitleAr: String = "",
    val topicSubTitleEn: String = "",
    val topicSubTitleAr: String = "",
    val topicTags: List<String> = emptyList(),
    val totalQuestions: Int = 0,
    val correctAnswersCount: Int = 0,
    val inCorrectAnswersCount: Int = totalQuestions - correctAnswersCount
)

