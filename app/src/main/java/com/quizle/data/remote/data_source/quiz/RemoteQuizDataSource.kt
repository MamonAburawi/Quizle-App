package com.quizle.data.remote.data_source.quiz

import com.quizle.data.remote.dto.IssueReportDto
import com.quizle.data.remote.dto.QuestionDto
import com.quizle.data.remote.dto.TopicDto
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result


interface RemoteQuizDataSource {

    suspend fun submitIssueReport(issueReport: IssueReportDto, token: String): Result<String, ServerDataError>

    suspend fun getQuizTopics(): Result<List<TopicDto>, ServerDataError>

    suspend fun loadTopicQuestions(topicId: String?): Result<List<QuestionDto>,ServerDataError>

    suspend fun getQuestionById(questionId: String): Result<QuestionDto,ServerDataError>

    suspend fun getTopicById(topicId: String): Result<TopicDto, ServerDataError>

    suspend fun searchTopics(query: String, limit: Int?): Result<List<TopicDto>, ServerDataError>

}