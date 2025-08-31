package com.quizle.presentation.screens.history



sealed interface HistoryAction {
    data class SearchButtonClicked(val query: String): HistoryAction
    data class TryQuizButtonClicked(val topicId: String): HistoryAction
    data class DeleteButtonClicked(val quizResultId: Int, val createdAt: Long): HistoryAction
}