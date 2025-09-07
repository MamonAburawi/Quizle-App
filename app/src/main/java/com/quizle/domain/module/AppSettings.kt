package com.quizle.domain.module

import kotlinx.serialization.Serializable


@Serializable
data class AppSettings(
    val isEnableNotificationApp: Boolean = false,
    val isEnableQuizTimeInMin: Boolean = false,
    val isEnableCustomTimeSwitch: Boolean = false,
    val customQuizTimeInMin: Int = 0,
    val language: String = "en"
)
