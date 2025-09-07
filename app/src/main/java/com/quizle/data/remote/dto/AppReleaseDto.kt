package com.quizle.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AppReleaseDto(
    @SerialName("id") val id: String,
    @SerialName("version_name") val versionName: String,
    @SerialName("version_code") val versionCode: Int,
    @SerialName("release_date") val releaseDate: Long?,
    @SerialName("release_note_Ar") val releaseNoteAr: String,
    @SerialName("release_note_En") val releaseNoteEn: String,
    @SerialName("download_link") val downloadLink: String
)
