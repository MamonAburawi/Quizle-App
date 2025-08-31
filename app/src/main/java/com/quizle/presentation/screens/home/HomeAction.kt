package com.quizle.presentation.screens.home


sealed interface HomeAction {

    data object EditProfileButtonClicked: HomeAction
    data object ResumeButtonClicked: HomeAction
    data object OnRefresh : HomeAction
    data class MorePopularQuizzesButtonClicked(val key: String) : HomeAction
    data class MoreTopViewedQuizzesButtonClicked(val key: String): HomeAction
    data class TopicItemCardClicked(val topicId: String): HomeAction
    data object NotificationButtonClicked: HomeAction
    data object UpdateButtonClicked: HomeAction

}