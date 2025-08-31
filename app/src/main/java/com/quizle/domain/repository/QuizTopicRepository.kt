package com.quizle.domain.repository

import com.quizle.domain.module.QuizTopic
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result

interface QuizTopicRepository {

    suspend fun loadTopics(): Result<List<QuizTopic>, DataError>

    suspend fun loadTopicById(topicId: String): Result<QuizTopic, DataError>

    suspend fun searchTopics(query: String, limit: Int?): Result<List<QuizTopic>, DataError>
}