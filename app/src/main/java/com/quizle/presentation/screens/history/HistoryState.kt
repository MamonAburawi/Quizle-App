package com.quizle.presentation.screens.history

import com.quizle.domain.module.QuizResult

data class HistoryState(
    val isLoading: Boolean = false,
    val quizResults: List<QuizResult> = emptyList(),
    val deleteLoadingStatus: Boolean = false,
    val initMessage: String = "",
    val error: String? = null
)