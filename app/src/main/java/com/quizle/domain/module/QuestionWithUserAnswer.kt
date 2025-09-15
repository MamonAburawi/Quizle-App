package com.quizle.domain.module

import androidx.room.Embedded
import kotlinx.serialization.Serializable


@Serializable
data class QuestionWithUserAnswer(
    val question: Question,
    val selectedOption: String?
)