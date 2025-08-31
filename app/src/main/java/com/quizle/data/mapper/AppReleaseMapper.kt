package com.quizle.data.mapper

import com.quizle.data.remote.dto.AppReleaseDto
import com.quizle.domain.module.AppRelease


fun AppRelease.toAppReleaseDto(): AppReleaseDto {
    return AppReleaseDto(
        id = id,
        versionName = versionName,
        versionCode = versionCode,
        releaseDate = releaseDate,
        releaseNoteAr = releaseNoteAr,
        releaseNoteEn = releaseNoteEn,
        downloadLink = downloadLink
    )
}



fun AppReleaseDto.toAppRelease(): AppRelease {
    return AppRelease(
        id = id,
        versionName = versionName,
        versionCode = versionCode,
        releaseDate = releaseDate,
        releaseNoteAr = releaseNoteAr,
        releaseNoteEn = releaseNoteEn,
        downloadLink = downloadLink
    )
}