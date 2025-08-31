package com.quizle.presentation.screens.topic

import com.quizle.domain.module.QuizTopic

data class TopicState(
    val topics: List<QuizTopic> = emptyList(),
    val initMessage: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)