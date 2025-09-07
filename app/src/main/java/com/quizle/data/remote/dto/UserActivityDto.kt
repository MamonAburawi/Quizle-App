package com.quizle.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserActivityDto(
    @SerialName("id") val id: String,
    @SerialName("user_name") val userName: String,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("action") val action: String,
    @SerialName("user_id") val userId: String
)

