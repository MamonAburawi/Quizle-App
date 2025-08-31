package com.quizle.presentation.screens.settings


data class SettingsState(
//    val user: User = User(),
    val isQuizTimeEnabled: Boolean = true,
    val isEnableCustomTimeSwitch: Boolean = false,
    val customTimeInMinutes: Int = 30,
    val selectedLanguage: String = "en",
    val isNotificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)


