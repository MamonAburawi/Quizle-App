package com.quizle.presentation.screens.login

import com.quizle.presentation.common.MessageType
import com.quizle.presentation.screens.sign_up.SignUpEvent


interface LoginEvent {

    data object NavigateToSignUpScreen: LoginEvent
    data object NavigateToDashboardScreen: LoginEvent
    data class ShowToastMessage(val message: String, val type: MessageType): LoginEvent

}