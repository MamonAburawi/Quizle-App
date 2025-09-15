package com.quizle.domain.repository

import com.quizle.data.utils.LocalDataError
import com.quizle.domain.module.Topic
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.module.TopicWithQuestions
import com.quizle.domain.utils.Result

interface TopicRepository {

    suspend fun loadTopics(): Result<List<Topic>, ServerDataError>

    suspend fun loadTopicById(topicId: String): Result<Topic, ServerDataError>

    suspend fun searchTopics(query: String, limit: Int?): Result<List<Topic>, ServerDataError>

//    suspend fun getTopicWithQuestions(topicId: String): Result<TopicWithQuestions, LocalDataError>
}