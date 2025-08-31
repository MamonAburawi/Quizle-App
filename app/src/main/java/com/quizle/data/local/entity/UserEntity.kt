package com.quizle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quizle.data.util.Constant.USER_TABLE_NAME
import kotlinx.serialization.Serializable



@Entity(tableName = USER_TABLE_NAME)
@Serializable
data class UserEntity(
    @PrimaryKey val id: String,
    val userName: String,
    val email: String,
    val password: String,
    val accountType: String,
    val phone: Long?,
    val token: Token,
    val gender: String?,
    val imageProfile: String?,
    val favoriteTopicsIds: List<String>,
    val likedTopicsIds: List<String>,
    val disLikedTopicsIds: List<String>,
    val resultQuizziesIds: List<String>,
    val timeSpentQuizzingInMin: Int,
    val totalCorrectAnswers: Int,
    val totalQuizzes: Int,
    val countryCode: String,
    val language: String,
    val isActive: Boolean,
    val isPublic: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val settings: Settings
) {
    @Serializable
    data class Token(
        val accessToken: String,
        val expAt: Long,
        val createdAt: Long,
        val type: String
    )

    @Serializable
    data class Settings(
        val enableNotificationApp: Boolean = true,
        val enableQuizTime: Boolean = true,
        val switchToCustomTimeInMin: Boolean = false,
        val customQuizTimeInMin: Int = 0
    )
}


//@Entity(tableName = USER_TABLE_NAME)
//data class UserEntity(
//    @PrimaryKey
//    val id: String,
//    val userName: String,
//    val email: String,
//    val password: String,
//    val phone: Long?,
//    val token: TokenEntity
//){
//    @Serializable
//    data class TokenEntity(
//        val accessToken: String,
//        val expAt: Long,
//        val createdAt: Long,
//        val type: String
//    )
//
//}