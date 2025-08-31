package com.quizle.presentation.screens.settings

import com.quizle.domain.module.User
import com.quizle.presentation.common.MessageType

sealed interface SettingsEvent {

    data class ShowToast(val message: String, val type: MessageType): SettingsEvent
    data object NavigateToSignUp : SettingsEvent
    data class RelaunchApp(val language: String): SettingsEvent


}