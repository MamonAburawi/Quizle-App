package com.quizle.presentation.screens.splash_screen

import com.quizle.presentation.common.MessageType


sealed interface SplashEvent {
    data object NavigateToLoginScreen: SplashEvent
    data object NavigateToDashboardScreen: SplashEvent
    data class ShowToastMessage(val message: String, val type: MessageType): SplashEvent
}

