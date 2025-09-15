package com.quizle.domain.module

import kotlinx.serialization.Serializable


@Serializable
data class UserAnswer(
    val questionId: String = "",
    val selectedOption: String = "",
    val topicId: String = ""
)