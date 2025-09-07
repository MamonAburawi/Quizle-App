package com.quizle.presentation.screens.topic

sealed interface TopicAction {

    data class SearchButtonClicked(val query: String): TopicAction
    data class TopicCardClicked(val topicId: String) : TopicAction


}