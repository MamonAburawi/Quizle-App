package com.quizle.domain.module


import kotlinx.serialization.Serializable


@Serializable
data class TopicWithQuestions(
    val topic: Topic,
    val questions: List<Question>
)