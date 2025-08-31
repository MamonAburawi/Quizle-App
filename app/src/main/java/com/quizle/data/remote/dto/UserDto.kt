package com.quizle.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("user_name") val userName: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("account_type") val accountType: String,
    @SerialName("phone") val phone: Long?,
    @SerialName("token") val token: Token,
    @SerialName("gender") val gender: String?,
    @SerialName("img_profile") val imageProfile: String?,
    @SerialName("favorite_topics_ids") val favoriteTopicsIds: List<String>,
    @SerialName("liked_topics_ids") val likedTopicsIds: List<String>,
    @SerialName("disliked_topics_ids") val disLikedTopicsIds: List<String>,
    @SerialName("result_quizzies_ids") val resultQuizziesIds: List<String>,
    @SerialName("time_spent_quizzing_in_min") val timeSpentQuizzingInMin: Int,
    @SerialName("total_correct_answers") val totalCorrectAnswers: Int,
    @SerialName("total_quizzes") val totalQuizzes: Int,
    @SerialName("country_code") val countryCode: String,
    @SerialName("language") val language: String,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("is_public") val isPublic: Boolean,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    @SerialName("settings") val settings: Settings
) {
    @Serializable
    data class Token(
        @SerialName("access_token") val accessToken: String,
        @SerialName("exp_at") val expAt: Long,
        @SerialName("created_at") val createdAt: Long,
        @SerialName("type") val type: String
    )

    @Serializable
    data class Settings(
        @SerialName("enable_notification_app") val enableNotificationApp: Boolean,
        @SerialName("enable_quiz_time") val enableQuizTime: Boolean,
        @SerialName("switch_to_custom_time_in_min") val switchToCustomTimeInMin: Boolean,
        @SerialName("custom_quiz_time_in_min") val customQuizTimeInMin: Int
    )
}


//@Serializable
//data class UserDto(
//    val id: String,
//    val userName: String,
//    val email: String,
//    val password: String,
//    val phone: Long? = null,
//    val token: TokenDto,
//    val gender: String? = null
//){
//    @Serializable
//    data class TokenDto(
//        val accessToken: String,
//        val expAt: Long,
//        val createdAt: Long,
//        val type: String
//    )
//}
//
