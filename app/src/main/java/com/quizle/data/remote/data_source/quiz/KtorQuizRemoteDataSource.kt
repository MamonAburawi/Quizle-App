package com.quizle.data.remote.data_source.quiz

import com.quizle.data.local.prefrences.AppPreferences
import com.quizle.data.remote.dto.IssueReportDto
import com.quizle.data.remote.dto.QuestionDto
import com.quizle.data.remote.dto.TopicDto
import com.quizle.data.utils.safeCall
import com.quizle.data.utils.Constant
import com.quizle.data.utils.Constant.ISSUE_REPORT_ROUTE
import com.quizle.data.utils.Constant.LIMIT_PARAMETER
import com.quizle.data.utils.Constant.QUESTIONS_ROUTE
import com.quizle.data.utils.Constant.SUB_TITLE_AR_PARAMETER
import com.quizle.data.utils.Constant.SUB_TITLE_EN_PARAMETER
import com.quizle.data.utils.Constant.TAG_PARAMETER
import com.quizle.data.utils.Constant.TITLE_AR_PARAMETER
import com.quizle.data.utils.Constant.TITLE_EN_PARAMETER
import com.quizle.data.utils.Constant.TOPICS_ROUTE
import com.quizle.data.utils.Constant.TOPICS_SEARCH_ROUTE
import com.quizle.data.utils.retryNetworkRequest
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result
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

    override suspend fun getQuizTopics(): Result< List<TopicDto>,ServerDataError> {
        return safeCall<List<TopicDto>> {
            retryNetworkRequest {
                httpClient.get(urlString = TOPICS_ROUTE){
                    header(HttpHeaders.AcceptLanguage, language)
                }
            }
        }
    }

    override suspend fun loadTopicQuestions(topicId: String?): Result<List<QuestionDto>, ServerDataError> {

        return safeCall<List<QuestionDto>> {
            retryNetworkRequest {
                httpClient.get(urlString = QUESTIONS_ROUTE) {
                    header(HttpHeaders.AcceptLanguage, language)
                    parameter(key = Constant.TOPIC_ID_PARAMETER, value = topicId)
                }
            }
        }
    }

    override suspend fun getQuestionById(questionId: String): Result<QuestionDto, ServerDataError> {
        return safeCall<QuestionDto> {
            retryNetworkRequest {
                httpClient.get(urlString = "$QUESTIONS_ROUTE/$questionId"){
                    header(HttpHeaders.AcceptLanguage, language)
                }
            }
        }
    }

    override suspend fun getTopicById(topicId: String): Result<TopicDto, ServerDataError> {
        return safeCall<TopicDto> {
            retryNetworkRequest {
                httpClient.get(urlString = "$TOPICS_ROUTE/$topicId"){
                    header(HttpHeaders.AcceptLanguage, language)
                }
            }
        }
    }

    override suspend fun searchTopics(query: String, limit: Int?): Result<List<TopicDto>, ServerDataError> {
        return safeCall<List<TopicDto>> {
            retryNetworkRequest {
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
    }


    override suspend fun submitIssueReport(issueReport: IssueReportDto, token: String): Result<String, ServerDataError> {
        return safeCall<String> {
            retryNetworkRequest {
                httpClient.post(urlString = ISSUE_REPORT_ROUTE){
                    header(HttpHeaders.AcceptLanguage, language)
                    bearerAuth(token)
                    setBody(issueReport)
                }
            }
        }
    }








}