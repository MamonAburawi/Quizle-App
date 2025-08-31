package com.quizle.presentation.util


import com.quizle.domain.module.AppRelease
import com.quizle.domain.module.QuizResult
import com.quizle.domain.module.QuizTopic
import java.util.Locale



private const val AR = "ar"


fun QuizTopic.title(): String {
    val currentLanguage = Locale.getDefault().language
    return if (currentLanguage == AR) {
        this.titleArabic
    } else {
        this.titleEnglish
    }
}

fun QuizTopic.subTitle(): String {
    val currentLanguage = Locale.getDefault().language
    return if (currentLanguage == AR) {
        this.subtitleArabic
    } else {
        this.subtitleEnglish
    }
}


fun AppRelease.releaseNote(): String {
    val currentLanguage = Locale.getDefault().language
    return if (currentLanguage == AR) {
        this.releaseNoteAr
    } else {
        this.releaseNoteEn
    }
}

fun QuizResult.topicTitle(): String {
    val currentLanguage = Locale.getDefault().language
    return if (currentLanguage == AR) {
        this.topicTitleAr
    } else {
        this.topicTitleEn
    }
}
