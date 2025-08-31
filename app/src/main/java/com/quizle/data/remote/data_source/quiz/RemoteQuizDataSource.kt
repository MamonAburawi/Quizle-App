package com.quizle.data.remote.data_source.quiz

import com.quizle.data.remote.dto.IssueReportDto
import com.quizle.data.remote.dto.QuizQuestionDto
import com.quizle.data.remote.dto.QuizTopicDto
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result


interface RemoteQuizDataSource {

    suspend fun submitIssueReport(issueReport: IssueReportDto, token: String): Result<String, DataError>

    suspend fun getQuizTopics(): Result<List<QuizTopicDto>, DataError>

    suspend fun loadTopicQuestions(topicId: String?): Result<List<QuizQuestionDto>,DataError>

    suspend fun getQuestionById(questionId: String): Result<QuizQuestionDto,DataError>

    suspend fun getTopicById(topicId: String): Result<QuizTopicDto, DataError>

    suspend fun searchTopics(query: String, limit: Int?): Result<List<QuizTopicDto>, DataError>

}