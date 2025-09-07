package com.quizle.domain.module


import kotlinx.serialization.Serializable


@Serializable
data class UserEvent(
    val id: String = "",
    val userName: String = "",
    val createdAt: Long = 0L,
    val action: String = "",
    val userId: String = ""
)

