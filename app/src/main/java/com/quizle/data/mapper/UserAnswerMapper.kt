package com.quizle.data.mapper

import com.quizle.data.local.entity.UserAnswerEntity
import com.quizle.domain.module.UserAnswer

fun UserAnswer.toUserAnswerEntity(): UserAnswerEntity {
    return UserAnswerEntity(
        questionId = questionId,
        selectedOption = selectedOption
    )
}

fun UserAnswerEntity.toUserAnswer(): UserAnswer {
    return UserAnswer(
        questionId = questionId,
        selectedOption = selectedOption
    )
}


fun List<UserAnswerEntity>.toUserAnswers(): List<UserAnswer> { return map { it.toUserAnswer() }}

fun List<UserAnswer>.toUserAnswerEntities(): List<UserAnswerEntity> { return map { it.toUserAnswerEntity() }}