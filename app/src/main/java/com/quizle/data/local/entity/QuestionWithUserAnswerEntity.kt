package com.quizle.data.local.entity

import androidx.room.Embedded

// This class holds the result of the JOIN query.
data class QuestionWithUserAnswerEntity(
    @Embedded
    val question: QuestionEntity,
    val selectedOption: String?
)