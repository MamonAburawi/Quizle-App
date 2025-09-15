package com.quizle.data.mapper

import com.quizle.data.local.entity.QuestionWithUserAnswerEntity
import com.quizle.domain.module.QuestionWithUserAnswer



fun QuestionWithUserAnswerEntity.toQuestionWithUserAnswer(): QuestionWithUserAnswer {
    return QuestionWithUserAnswer(
        question = question.entityToQuizQuestion(),
        selectedOption = selectedOption
    )
}