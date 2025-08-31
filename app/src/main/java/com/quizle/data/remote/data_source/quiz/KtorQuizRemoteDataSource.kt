package com.quizle.data.remote.data_source.quiz

import android.util.Printer
import com.quizle.data.local.prefrences.AppPreferences
import com.quizle.data.remote.dto.IssueReportDto
import com.quizle.data.remote.dto.QuizQuestionDto
import com.quizle.data.remote.dto.QuizTopicDto
import com.quizle.data.remote.safeCall
import com.quizle.data.util.Constant
import com.quizle.data.util.Constant.ISSUE_REPORT_ROUTE
import com.quizle.data.util.Constant.LIMIT_PARAMETER
import com.quizle.data.util.Constant.QUESTIONS_ROUTE
import com.quizle.data.util.Constant.SUB_TITLE_AR_PARAMETER
import com.quizle.data.util.Constant.SUB_TITLE_EN_PARAMETER
import com.quizle.data.util.Constant.TAG_PARAMETER
import com.quizle.data.util.Constant.TITLE_AR_PARAMETER
import com.quizle.data.util.Constant.TITLE_EN_PARAMETER
import com.quizle.data.util.Constant.TOPICS_ROUTE
import com.quizle.data.util.Constant.TOPICS_SEARCH_ROUTE
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class KtorQuizRemoteDataSource(
    private val httpClient: HttpClient,
    private val appPreferences: AppPreferences
): RemoteQuizDataSource {

    private val language = appPreferences.loadSettings().language

    override suspend fun getQuizTopics(): Result< List<QuizTopicDto>,DataError> {
        return safeCall<List<QuizTopicDto>> {
            httpClient.get(urlString = TOPICS_ROUTE){
                header(HttpHeaders.AcceptLanguage, language)
            }
        }
    }

    override suspend fun loadTopicQuestions(topicId: String?): Result< List<QuizQuestionDto>,DataError> {
        return safeCall<List<QuizQuestionDto>> {
            httpClient.get(urlString = QUESTIONS_ROUTE){
                header(HttpHeaders.AcceptLanguage, language)
                parameter(key = Constant.TOPIC_ID_PARAMETER, value = topicId)

            }
        }
    }

    override suspend fun getQuestionById(questionId: String): Result<QuizQuestionDto, DataError> {
        return safeCall<QuizQuestionDto> {
            httpClient.get(urlString = "$QUESTIONS_ROUTE/$questionId"){
                header(HttpHeaders.AcceptLanguage, language)
            }
        }
    }

    override suspend fun getTopicById(topicId: String): Result<QuizTopicDto, DataError> {
        return safeCall<QuizTopicDto> {
            httpClient.get(urlString = "$TOPICS_ROUTE/$topicId"){
                header(HttpHeaders.AcceptLanguage, language)
            }
        }
    }

    override suspend fun searchTopics(query: String, limit: Int?): Result<List<QuizTopicDto>, DataError> {
        return safeCall<List<QuizTopicDto>> {
            httpClient.get(urlString = TOPICS_SEARCH_ROUTE){
                header(HttpHeaders.AcceptLanguage, language)
                parameter(key = TITLE_EN_PARAMETER, value = query)
                parameter(key = TITLE_AR_PARAMETER, value = query)
                parameter(key = SUB_TITLE_EN_PARAMETER, value = query)
                parameter(key = SUB_TITLE_AR_PARAMETER, value = query)
                parameter(key = TAG_PARAMETER, value = query)
                parameter(key = LIMIT_PARAMETER, value = limit)
            }
        }
    }


    override suspend fun submitIssueReport(issueReport: IssueReportDto, token: String): Result<String, DataError> {
        return safeCall<String> {
            httpClient.post(urlString = ISSUE_REPORT_ROUTE){
                header(HttpHeaders.AcceptLanguage, language)
                bearerAuth(token)
                setBody(issueReport)
            }
        }
    }








}