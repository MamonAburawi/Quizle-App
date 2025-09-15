package com.quizle.domain.repository

import com.quizle.domain.module.Question
import com.quizle.domain.module.UserAnswer
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result

interface QuestionRepository {

    suspend fun loadQuizQuestions(topicId: String? = null): Result<List<Question>, ServerDataError>

//    suspend fun loadQuizQuestions(): Result<List<QuizQuestion>, DataError>

    suspend fun getQuestionById(questionId: String): Result<Question?, ServerDataError>

    suspend fun saveUserAnswers(userAnswers: List<UserAnswer>): Result<Unit, ServerDataError>

    suspend fun loadUserAnswersByTopicId(topicId: String): Result<List<UserAnswer>, ServerDataError>

}