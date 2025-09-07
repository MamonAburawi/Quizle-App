package com.quizle.presentation.screens.topic

import com.quizle.domain.module.Topic

data class TopicState(
    val topics: List<Topic> = emptyList(),
    val isInit: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)