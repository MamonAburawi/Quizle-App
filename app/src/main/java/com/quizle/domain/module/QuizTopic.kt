package com.quizle.domain.module


import kotlinx.serialization.Serializable

@Serializable
data class QuizTopic(
    val id: String = "",
    val titleArabic: String = "",
    val titleEnglish: String = "",
    val subtitleArabic: String = "",
    val subtitleEnglish: String = "",
    val masterOwnerId: String = "",
    val ownersIds: List<String> = emptyList(),
    val topicColor: String = "",
    val imageUrl: String = "",
    val tags: List<String> = emptyList(),
    val viewsCount: Int = 0,
    val likeCount: Int = 0,
    val disLikeCount: Int = 0,
    val playedCount: Int = 0,
    val quizTimeInMin: Int? = 0,
    val isDeleted: Boolean = false,
    val isPublic: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)


