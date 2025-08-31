package com.quizle.domain.module

import kotlinx.serialization.Serializable


@Serializable
data class ErrorResponse(
    val code: Int,
    val message: String?
)
