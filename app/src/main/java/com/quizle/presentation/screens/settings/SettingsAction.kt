package com.quizle.presentation.screens.settings

sealed interface SettingsAction {
    data object EnableQuizTimeButtonClicked: SettingsAction
    data object  EnableCustomTimeButtonClicked: SettingsAction
    data class  CustomTimeChanged(val minutes: Int): SettingsAction
    data class  LanguageButtonClicked(val language: String): SettingsAction
    data object  NotificationsButtonClicked: SettingsAction

    data object SaveButtonClicked: SettingsAction

    data object LogoutButtonClicked: SettingsAction

}