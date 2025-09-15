package com.quizle.presentation.screens.result

import com.quizle.data.local.entity.QuestionWithUserAnswerEntity
import com.quizle.domain.module.Question
import com.quizle.domain.module.QuestionWithUserAnswer
import com.quizle.domain.module.Topic
import com.quizle.domain.module.UserAnswer
import kotlinx.serialization.Serializable


@Serializable
data class ResultState(
    val totalQuestionsCount: Int = 0,
    val correctAnswersCount: Int = 0,
    val topic: Topic = Topic(),
    val loadingMessage: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val questionsWithAnswers: List<QuestionWithUserAnswer> = emptyList(),
    )