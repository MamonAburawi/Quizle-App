package com.quizle.domain.module

import kotlinx.serialization.Serializable


@Serializable
data class AppRelease(
    val id: String = "",
    val versionName: String = "",
    val versionCode: Int = 0,
    val releaseDate: Long? = null,
    val releaseNoteAr: String = "",
    val releaseNoteEn: String = "",
    val downloadLink: String = ""
)
