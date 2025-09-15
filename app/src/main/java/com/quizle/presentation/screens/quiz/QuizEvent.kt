package com.quizle.presentation.screens.quiz

import com.quizle.presentation.common.MessageType

sealed interface QuizEvent {
    data class ShowToast(val message: String, val type: MessageType): QuizEvent
    data class NavigateToResultScreen(val topicId: String): QuizEvent
    data object NavigateToDashboardScreen: QuizEvent
    data object NavigateToTopicScreen: QuizEvent
}