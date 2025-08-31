package com.quizle.domain.repository


import com.quizle.domain.module.QuizResult
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result

interface QuizResultRepository {

    suspend fun saveQuizResult(quizResult: QuizResult): Result<Unit, DataError>

    suspend fun getQuizResultById(id: String): Result<QuizResult, DataError>

    suspend fun getAllQuizResults(): Result<List<QuizResult>, DataError>

    suspend fun deleteQuizResult(quizResultId: Int, createdAt: Long): Result<Unit, DataError>

    suspend fun deleteAllQuizResults(): Result<Unit, DataError>

    suspend fun searchQuizResults(query: String?): Result<List<QuizResult>, DataError>

}