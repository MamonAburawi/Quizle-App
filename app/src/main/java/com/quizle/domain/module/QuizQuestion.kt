package com.quizle.domain.module

import kotlinx.serialization.Serializable


@Serializable
data class QuizQuestion(
    val id: String = "",
    val ownersIds: List<String> = emptyList(),
    val masterOwnerId: String,
    val questionText: String,
    val correctAnswer: String,
    val imageUrl: String? = null,
    val topicId: String,
    val allOptions: List<String>,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val reportCount: Int = 0,
    val level: String = "",
    val tags: List<String> = emptyList(),
    val explanation: String = ""
)
