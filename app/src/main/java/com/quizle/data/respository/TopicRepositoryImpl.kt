package com.quizle.data.respository

import com.quizle.data.local.dao.TopicDao
import com.quizle.data.mapper.entityQuizTopics
import com.quizle.data.mapper.entityToQuizTopics
import com.quizle.data.mapper.toQuizTopic
import com.quizle.data.mapper.toQuizTopics
import com.quizle.data.remote.data_source.quiz.RemoteQuizDataSource
import com.quizle.domain.module.Topic
import com.quizle.domain.repository.TopicRepository
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result
import com.quizle.domain.utils.Result.*


class TopicRepositoryImpl(
    private val remoteSource: RemoteQuizDataSource,
    private val localSource: TopicDao,
) : TopicRepository {

    override suspend fun loadTopics(): Result<List<Topic>,ServerDataError> {
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
                     Failure(ServerDataError.ConflictServerDataType)
                 }
             }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }


    override suspend fun loadTopicById(topicId: String): Result<Topic, ServerDataError> {
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
                     Failure(ServerDataError.ConflictServerDataType)
                 }
             }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }

    }


    // remote search only
    override suspend fun searchTopics(query: String, limit: Int?): Result<List<Topic>, ServerDataError> {
        return try {
            when (val result = remoteSource.searchTopics(query, limit)) {
                is Success -> {
                    val topics = result.data.toQuizTopics()
                    if (topics.isNotEmpty()){
                        Success(topics)
                    }else {
                        Failure(ServerDataError.NotFound)
                    }
                }
                is Failure -> {
                    Failure(result.error)
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




}