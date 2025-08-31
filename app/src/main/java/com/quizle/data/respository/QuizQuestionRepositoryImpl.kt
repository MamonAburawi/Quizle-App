package com.quizle.data.respository


import com.quizle.data.local.dao.QuizQuestionDao
import com.quizle.data.local.dao.UserAnswerDao
import com.quizle.data.mapper.entityToQuizQuestion
import com.quizle.data.mapper.entityToQuizQuestions
import com.quizle.data.mapper.quizQuestionToEntity
import com.quizle.data.mapper.toQuizQuestion
import com.quizle.data.mapper.toQuizQuestions
import com.quizle.data.mapper.toUserAnswerEntities
import com.quizle.data.mapper.toUserAnswers
import com.quizle.data.remote.data_source.quiz.RemoteQuizDataSource
import com.quizle.domain.module.QuizQuestion
import com.quizle.domain.module.UserAnswer
import com.quizle.domain.repository.QuizQuestionRepository
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result
import com.quizle.domain.util.Result.*


class QuizQuestionRepositoryImpl(
    private val remoteSource: RemoteQuizDataSource,
    private val userAnswersDao: UserAnswerDao,
    private val quizQuestionDao: QuizQuestionDao
): QuizQuestionRepository{


    override suspend fun loadQuizQuestions(topicId: String?): Result<List<QuizQuestion>, DataError> {
        return try {
            when(val result = remoteSource.loadTopicQuestions(topicId)){
                is Success -> {
                    val questions = result.data.toQuizQuestions()
//                quizQuestionDao.clearAllQuizQuestions()
                    quizQuestionDao.insertQuizQuestions(questions.quizQuestionToEntity())
                    Success(questions)
                }
                is Failure -> {
                    val cachedQuestions = quizQuestionDao.getAllQuizQuestion().entityToQuizQuestions()
                    if (cachedQuestions.isNotEmpty()){
                        Success(cachedQuestions)
                    }else {
                        Failure(result.error)
                    }
                }
                is SuccessMessage -> {
                    Failure(DataError.ConflictDataType)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(DataError.Unknown)
        }
    }


    override suspend fun getQuestionById(questionId: String): Result<QuizQuestion, DataError> {
        return try {
            when(val result = remoteSource.getQuestionById(questionId)){
                is Success -> {
                    val question = result.data.toQuizQuestion()
                    Success(question)
                }

                is Failure -> {
                    val cachedQuestion = quizQuestionDao.getQuizQuestionById(questionId)?.entityToQuizQuestion()
                    if (cachedQuestion != null){
                        Success(cachedQuestion)
                    }else {
                        Failure(result.error)
                    }
                }

                is SuccessMessage -> {
                    Failure(DataError.ConflictDataType)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(DataError.Unknown)
        }
    }


    override suspend fun saveUserAnswers(userAnswers: List<UserAnswer>): Result<Unit, DataError> {
        return try {
            userAnswersDao.clearAllUserAnswers()
            userAnswersDao.insertUserAnswers(userAnswers.toUserAnswerEntities())
            Success(Unit)
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(DataError.Unknown)
        }
    }


    override suspend fun loadUserAnswers(): Result<List<UserAnswer>, DataError> {
        return try {
            val cachedUserAnswers = userAnswersDao.getAllUserAnswers()
            if (cachedUserAnswers.isNotEmpty()){
                Result.Success(cachedUserAnswers.toUserAnswers())
            }else {
                Result.Failure(DataError.NotFound)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Result.Failure(DataError.Unknown)
        }
    }

}