package com.quizle.presentation.dashboard


data class DashboardState(
    val userName: String = "",
    val questionAttempted: Int = 0,
    val correctAnswers: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val topics: List<com.quizle.domain.module.Topic> = emptyList()
)