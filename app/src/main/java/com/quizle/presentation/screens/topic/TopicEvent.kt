package com.quizle.presentation.screens.topic

import com.quizle.presentation.common.MessageType

sealed interface TopicEvent {
    data class ShowMessage(val message: String, val type: MessageType): TopicEvent
    data class NavigateToQuiz(val topicId: String): TopicEvent
}

