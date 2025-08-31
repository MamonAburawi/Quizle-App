package com.quizle.domain.repository

import com.quizle.domain.module.QuizQuestion
import com.quizle.domain.module.UserAnswer
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result

interface QuizQuestionRepository {

    suspend fun loadQuizQuestions(topicId: String? = null): Result<List<QuizQuestion>, DataError>

//    suspend fun loadQuizQuestions(): Result<List<QuizQuestion>, DataError>

    suspend fun getQuestionById(questionId: String): Result<QuizQuestion?, DataError>

    suspend fun saveUserAnswers(userAnswers: List<UserAnswer>): Result<Unit, DataError>

    suspend fun loadUserAnswers(): Result<List<UserAnswer>, DataError>

}