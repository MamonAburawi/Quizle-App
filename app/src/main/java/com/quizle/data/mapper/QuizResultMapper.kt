package com.quizle.data.mapper

import com.quizle.data.local.entity.QuizResultEntity
import com.quizle.domain.module.QuizQuestion
import com.quizle.domain.module.QuizResult
import com.quizle.domain.module.QuizTopic
import com.quizle.domain.module.UserAnswer


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