package com.quizle.presentation.screens.profile

import com.quizle.presentation.common.MessageType

sealed interface ProfileEvent {

    data class ShowMessage(val message: String, val type: MessageType): ProfileEvent

    data object NavigateToHomeScreen: ProfileEvent

}