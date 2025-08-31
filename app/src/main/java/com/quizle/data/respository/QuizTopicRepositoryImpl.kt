package com.quizle.data.respository

import com.quizle.data.local.dao.QuizTopicDao
import com.quizle.data.mapper.entityQuizTopics
import com.quizle.data.mapper.entityToQuizTopics
import com.quizle.data.mapper.toQuizTopic
import com.quizle.data.mapper.toQuizTopics
import com.quizle.data.remote.data_source.quiz.RemoteQuizDataSource
import com.quizle.domain.module.QuizTopic
import com.quizle.domain.repository.QuizTopicRepository
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result
import com.quizle.domain.util.Result.*
import com.quizle.presentation.util.StringProvider


class QuizTopicRepositoryImpl(
    private val remoteSource: RemoteQuizDataSource,
    private val localSource: QuizTopicDao,
) : QuizTopicRepository {

    override suspend fun loadTopics(): Result<List<QuizTopic>,DataError> {
        return try {
             when(val result = remoteSource.getQuizTopics()){
                is Result.Success -> {
                    val topicDto = result.data
                    localSource.clearAllQuizTopic()
                    localSource.insertQuizTopics(topicDto.entityQuizTopics())
                    Success(topicDto.toQuizTopics())
                }
                is Result.Failure -> {
                    val cachedTopics = localSource.getAllQuizTopic()
                    return if (cachedTopics.isNotEmpty()){
                        Success(cachedTopics.entityToQuizTopics())
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


    override suspend fun loadTopicById(topicId: String): Result<QuizTopic, DataError> {
        return try {
             when(val result = remoteSource.getTopicById(topicId)){
                is Success-> {
                    val topic = result.data.toQuizTopic()
                    Success(topic)
                }
                is Failure -> {
                    val localTopic = localSource.getQuizTopicByTopicId(topicId)
                    if (localTopic != null){
                        Success(localTopic.toQuizTopic())
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


    // remote search only
    override suspend fun searchTopics(query: String, limit: Int?): Result<List<QuizTopic>, DataError> {
        return try {
            when (val result = remoteSource.searchTopics(query, limit)) {
                is Success -> {
                    val topics = result.data.toQuizTopics()
                    if (topics.isNotEmpty()){
                        Success(topics)
                    }else {
                        Failure(DataError.NotFound)
                    }
                }
                is Failure -> {
                    Failure(result.error)
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




}