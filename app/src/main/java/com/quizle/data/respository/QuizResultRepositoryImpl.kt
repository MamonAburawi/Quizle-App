package com.quizle.data.respository

import com.quizle.data.local.dao.QuizResultDao
import com.quizle.data.mapper.toQuizResult
import com.quizle.data.mapper.toQuizResultEntity
import com.quizle.domain.module.QuizResult
import com.quizle.domain.repository.QuizResultRepository
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result
import com.quizle.presentation.util.StringProvider

class QuizResultRepositoryImpl(
    private val quizResultDao: QuizResultDao,
    private val stringProvider: StringProvider
): QuizResultRepository {

    override suspend fun saveQuizResult(quizResult: QuizResult): Result<Unit, DataError> {
        return try {
            val quizResultEntity = quizResult.toQuizResultEntity()
            quizResultDao.insertQuizResult(quizResultEntity)
            Result.Success(Unit)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Failure(DataError.Unknown)
        }
    }


    override suspend fun getQuizResultById(id: String): Result<QuizResult, DataError> {
        return try {
            val quizResultEntity = quizResultDao.getQuizResultById(id)
            if (quizResultEntity != null){
                Result.Success(quizResultEntity.toQuizResult())
            } else {
                Result.Failure(DataError.NotFound)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Result.Failure(DataError.Unknown)
        }
    }


    override suspend fun getAllQuizResults(): Result<List<QuizResult>, DataError> {
        return try {
            val quizResults = quizResultDao.getAllQuizResults().map { it.toQuizResult() }
            if (quizResults.isNotEmpty()){
                Result.Success(quizResults)
            }else{
                 Result.Failure(DataError.NotFound)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Failure(DataError.Unknown)
        }
    }

    override suspend fun deleteQuizResult(quizResultId: Int,createdAt: Long): Result<Unit, DataError> {
        return try {
            quizResultDao.deleteQuizResult(quizResultId, createdAt)
            Result.Success(Unit)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Failure(DataError.Unknown)
        }
    }


    override suspend fun deleteAllQuizResults(): Result<Unit, DataError> {
        return try {
            quizResultDao.deleteAllQuizResults()
            Result.Success(Unit)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Failure(DataError.Unknown)
        }
    }

    override suspend fun searchQuizResults(query: String?): Result<List<QuizResult>, DataError> {
        return try {
            val quizResults = quizResultDao.searchQuizResults(query).map { it.toQuizResult() }
            if (quizResults.isNotEmpty()){
                Result.Success(quizResults)
            }else{
                Result.Failure(DataError.NotFound)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Result.Failure(DataError.Unknown)
        }
    }

}

