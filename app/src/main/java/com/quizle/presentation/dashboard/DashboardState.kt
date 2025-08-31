package com.quizle.presentation.dashboard

import android.adservices.topics.Topic
import com.quizle.domain.module.QuizTopic


data class DashboardState(
    val userName: String = "",
    val questionAttempted: Int = 0,
    val correctAnswers: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val topics: List<QuizTopic> = emptyList()
)