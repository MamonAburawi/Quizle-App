package com.quizle.presentation.screens.home

import com.quizle.presentation.common.MessageType

sealed interface HomeEvent {

    data class NavigateToTopicScreen(val key: String): HomeEvent
    data class NavigateToQuizScreen(val topicId: String): HomeEvent
    data object NavigateToProfileScreen: HomeEvent
    data class ShowToast(val message: String, val type: MessageType): HomeEvent

    data object NavigateToNotificationScreen: HomeEvent

    data class NavigateToUrlBrowser(val url: String): HomeEvent
}