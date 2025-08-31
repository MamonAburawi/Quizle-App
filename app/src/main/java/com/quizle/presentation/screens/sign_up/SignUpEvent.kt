package com.quizle.presentation.screens.sign_up

import com.quizle.presentation.common.MessageType


sealed interface SignUpEvent {

    data object NavigateToLoginScreen: SignUpEvent
    data object NavigateToDashboardScreen: SignUpEvent
    data class ShowToastMessage(val message: String, val type: MessageType): SignUpEvent
}

