package com.quizle.domain.repository


import com.quizle.data.utils.LocalDataError
import com.quizle.domain.module.QuizResult
import com.quizle.domain.utils.Result

interface QuizResultRepository {

    suspend fun saveQuizResult(quizResult: QuizResult): Result<Unit, LocalDataError>

    suspend fun getQuizResultById(id: String): Result<QuizResult, LocalDataError>

    suspend fun getAllQuizResults():  Result<List<QuizResult>, LocalDataError>

    suspend fun deleteQuizResult(quizResultId: Int, createdAt: Long): Result<Unit, LocalDataError>

    suspend fun deleteAllQuizResults(): Result<Unit, LocalDataError>

    suspend fun searchQuizResults(query: String?): Result<List<QuizResult>, LocalDataError>

}