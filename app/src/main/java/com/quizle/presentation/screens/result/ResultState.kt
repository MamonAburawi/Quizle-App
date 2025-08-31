package com.quizle.presentation.screens.result

import com.quizle.domain.module.QuizQuestion
import com.quizle.domain.module.QuizTopic
import com.quizle.domain.module.UserAnswer
import kotlinx.serialization.Serializable


@Serializable
data class ResultState(
    val scorePercentage: Int = 0,
    val totalQuestionsCount: Int = 0,
    val correctAnswersCount: Int = 0,
    val topic: QuizTopic = QuizTopic(),
    val loadingMessage: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val questions: List<QuizQuestion> = emptyList(),
    val answers: List<UserAnswer> = emptyList(),
)