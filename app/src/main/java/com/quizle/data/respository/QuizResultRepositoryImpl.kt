package com.quizle.data.respository

import com.quizle.data.local.dao.QuizResultDao
import com.quizle.data.local.entity.QuestionWithUserAnswerEntity
import com.quizle.data.mapper.toQuestionWithUserAnswer
import com.quizle.data.mapper.toQuizResult
import com.quizle.data.mapper.toQuizResultEntity
import com.quizle.data.utils.LocalDataError
import com.quizle.domain.module.QuestionWithUserAnswer
import com.quizle.domain.module.QuizResult
import com.quizle.domain.repository.QuizResultRepository
import com.quizle.domain.utils.Result


/** currently all data from this repo is only managed locally **/

class QuizResultRepositoryImpl(
    private val quizResultDao: QuizResultDao,
): QuizResultRepository {

    override suspend fun saveQuizResult(quizResult: QuizResult): Result<Unit, LocalDataError> {
        return try {
            val quizResultEntity = quizResult.toQuizResultEntity()
            quizResultDao.insertQuizResult(quizResultEntity)
            Result.Success(Unit)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Failure(LocalDataError.Unknown)
        }
    }


    override suspend fun getQuizResultById(id: String): Result<QuizResult, LocalDataError> {
        return try {
            val quizResultEntity = quizResultDao.getQuizResultById(id)
            if (quizResultEntity != null){
                Result.Success(quizResultEntity.toQuizResult())
            } else {
                Result.Failure(LocalDataError.NoQuizResultFound)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Result.Failure(LocalDataError.Unknown)
        }
    }


    override suspend fun getAllQuizResults(): Result<List<QuizResult>, LocalDataError> {
        return try {
            val quizResults = quizResultDao.getAllQuizResults().map { it.toQuizResult() }
            if (quizResults.isNotEmpty()){
                Result.Success(quizResults)
            }else{
                 Result.Failure(LocalDataError.NoQuizResultFound)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Failure(LocalDataError.Unknown)
        }
    }

    override suspend fun deleteQuizResult(quizResultId: Int,createdAt: Long): Result<Unit, LocalDataError> {
        return try {
            quizResultDao.deleteQuizResult(quizResultId, createdAt)
            Result.Success(Unit)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Failure(LocalDataError.Unknown)
        }
    }


    override suspend fun deleteAllQuizResults(): Result<Unit, LocalDataError> {
        return try {
            quizResultDao.deleteAllQuizResults()
            Result.Success(Unit)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Failure(LocalDataError.Unknown)
        }
    }

    override suspend fun searchQuizResults(query: String?): Result<List<QuizResult>, LocalDataError> {
        return try {
            val quizResults = quizResultDao.searchQuizResults(query).map { it.toQuizResult() }
            if (quizResults.isNotEmpty()){
                Result.Success(quizResults)
            }else{
                Result.Failure(LocalDataError.NoQuizResultFound)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Result.Failure(LocalDataError.Unknown)
        }
    }

    override suspend fun getQuestionsWithAnswersByTopicId(topicId: String): Result<List<QuestionWithUserAnswer>, LocalDataError> {
        return try {
            val questionsWithAnswers = quizResultDao.getQuestionsWithAnswersByTopicId(topicId)
            if (questionsWithAnswers.isNotEmpty()) {
                Result.Success(questionsWithAnswers.map { it.toQuestionWithUserAnswer() })
            } else {
                Result.Failure(LocalDataError.NoQuizResultFound)
            }

        } catch (ex: Exception){
            ex.printStackTrace()
            Result.Failure(LocalDataError.Unknown)
        }
    }

}

