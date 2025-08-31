package com.quizle.presentation.screens.history

import com.quizle.presentation.common.MessageType

sealed interface HistoryEvent {
    data class ShowMessage(val message: String, val type: MessageType) : HistoryEvent
    data class NavigateToQuiz(val topicId: String): HistoryEvent
}