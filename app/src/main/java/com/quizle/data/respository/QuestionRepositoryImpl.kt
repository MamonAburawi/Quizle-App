package com.quizle.data.respository


import com.quizle.data.local.dao.QuestionDao
import com.quizle.data.local.dao.UserAnswerDao
import com.quizle.data.mapper.entityToQuizQuestion
import com.quizle.data.mapper.entityToQuizQuestions
import com.quizle.data.mapper.quizQuestionToEntity
import com.quizle.data.mapper.toQuizQuestion
import com.quizle.data.mapper.toQuizQuestions
import com.quizle.data.mapper.toUserAnswerEntities
import com.quizle.data.mapper.toUserAnswers
import com.quizle.data.remote.data_source.quiz.RemoteQuizDataSource
import com.quizle.domain.module.Question
import com.quizle.domain.module.UserAnswer
import com.quizle.domain.repository.QuestionRepository
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result
import com.quizle.domain.utils.Result.*


class QuestionRepositoryImpl(
    private val remoteSource: RemoteQuizDataSource,
    private val userAnswersDao: UserAnswerDao,
    private val questionDao: QuestionDao
): QuestionRepository{


    override suspend fun loadQuizQuestions(topicId: String?): Result<List<Question>, ServerDataError> {
        return try {
            when(val result = remoteSource.loadTopicQuestions(topicId)){
                is Success -> {
                    val questions = result.data.toQuizQuestions()
//                quizQuestionDao.clearAllQuizQuestions()
                    questionDao.insertQuizQuestions(questions.quizQuestionToEntity())
                    Success(questions)
                }
                is Failure -> {
                    val cachedQuestions = topicId?.let { questionDao.getQuizQuestionsByTopicId(it) }
                        ?.entityToQuizQuestions() ?: emptyList()
                    if (cachedQuestions.isNotEmpty()){
                        Success(cachedQuestions)
                    }else {
                        Failure(result.error)
                    }
                }
                is SuccessMessage -> {
                    Failure(ServerDataError.ConflictServerDataType)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }


    override suspend fun getQuestionById(questionId: String): Result<Question, ServerDataError> {
        return try {
            when(val result = remoteSource.getQuestionById(questionId)){
                is Success -> {
                    val question = result.data.toQuizQuestion()
                    Success(question)
                }

                is Failure -> {
                    val cachedQuestion = questionDao.getQuizQuestionById(questionId)?.entityToQuizQuestion()
                    if (cachedQuestion != null){
                        Success(cachedQuestion)
                    }else {
                        Failure(result.error)
                    }
                }

                is SuccessMessage -> {
                    Failure(ServerDataError.ConflictServerDataType)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }


    override suspend fun saveUserAnswers(userAnswers: List<UserAnswer>): Result<Unit, ServerDataError> {
        return try {
            userAnswersDao.clearAllUserAnswers()
            userAnswersDao.insertUserAnswers(userAnswers.toUserAnswerEntities())
            Success(Unit)
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }


    override suspend fun loadUserAnswers(): Result<List<UserAnswer>, ServerDataError> {
        return try {
            val cachedUserAnswers = userAnswersDao.getAllUserAnswers()
            if (cachedUserAnswers.isNotEmpty()){
                Result.Success(cachedUserAnswers.toUserAnswers())
            }else {
                Result.Failure(ServerDataError.NotFound)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Result.Failure(ServerDataError.Unknown)
        }
    }

}