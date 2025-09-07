package com.quizle.presentation.screens.history

import com.quizle.domain.module.QuizResult

data class HistoryState(
    val isLoading: Boolean = false,
    val quizResults: List<QuizResult> = emptyList(),
    val deleteLoadingStatus: Boolean = false,
    val isInit: Boolean = true,
    val error: String? = null
)