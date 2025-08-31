package com.quizle.domain.module

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val accountType: String = "",
    val gender: String? = null,
    val phone: Long? = null,
    val token: Token = Token(),
    val imageProfile: String? = null,
    val favoriteTopicsIds: List<String> = emptyList(),
    val likedTopicsIds: List<String> = emptyList(),
    val disLikedTopicsIds: List<String> = emptyList(),
    val resultQuizziesIds: List<String> = emptyList(),
    val timeSpentQuizzingInMin: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalQuizzes: Int = 0,
    val countryCode: String = "",
    val language: String = "en",
    val isActive: Boolean = true,
    val isPublic: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val settings: Settings = Settings()
) {
    @Serializable
    data class Token(
        val accessToken: String = "",
        val expAt: Long = 0L,
        val createdAt: Long = 0L,
        val type: String = ""
    )

    @Serializable
    data class Settings(
        val enableNotificationApp: Boolean = true,
        val enableQuizTime: Boolean = true,
        val switchToCustomTimeInMin: Boolean = false,
        val customQuizTimeInMin: Int = 0
    )
}
