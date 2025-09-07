package com.quizle.data.mapper

import com.quizle.data.local.entity.QuizResultEntity
import com.quizle.domain.module.QuizResult


fun QuizResultEntity.toQuizResult(): QuizResult {
    return QuizResult(
        id = id,
        createdAt = createdAt,
        topicId = topicId,
        totalQuestions = totalQuestions,
        correctAnswersCount = correctAnswersCount,
        inCorrectAnswersCount = inCorrectAnswersCount,
        topicTitleEn = topicTitleEn,
        topicTitleAr = topicTitleAr,
        topicSubTitleEn = topicSubTitleEn,
        topicSubTitleAr = topicSubTitleAr,
        topicTags = topicTags
    )
}



fun QuizResult.toQuizResultEntity(): QuizResultEntity {
    return QuizResultEntity(
        createdAt = createdAt,
        topicId = topicId,
        totalQuestions = totalQuestions,
        correctAnswersCount = correctAnswersCount,
        inCorrectAnswersCount = inCorrectAnswersCount,
        topicTitleEn = topicTitleEn,
        topicTitleAr = topicTitleAr,
        topicSubTitleEn = topicSubTitleEn,
        topicSubTitleAr = topicSubTitleAr,
        topicTags = topicTags,

    )
}