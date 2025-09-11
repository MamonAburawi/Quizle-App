package com.quizle.data.remote.data_source.user

import android.net.Uri
import android.util.Log
import com.quizle.data.local.prefrences.AppPreferences

import com.quizle.data.remote.dto.LogEventDto
import com.quizle.data.remote.dto.UserDto
import com.quizle.data.utils.safeCall
import com.quizle.data.utils.Constant.IMAGE_URL_PARAMETER
import com.quizle.data.utils.Constant.LOG_USER_ACTIVITY_ROUTE
import com.quizle.data.utils.Constant.USER_ACTION_PARAMETER
import com.quizle.data.utils.Constant.USER_UPDATE_ROUTE
import com.quizle.data.utils.Constant.USER_EMAIL_PARAMETER
import com.quizle.data.utils.Constant.USER_ID_PARAMETER
import com.quizle.data.utils.Constant.USER_IMAGE_PROFILE_ADD_ROUTE
import com.quizle.data.utils.Constant.USER_IMAGE_PROFILE_DELETE_ROUTE
import com.quizle.data.utils.Constant.USER_IMAGE_PROFILE_ROUTE
import com.quizle.data.utils.Constant.USER_LOGIN_ROUTE
import com.quizle.data.utils.Constant.USER_LOGOUT_ROUTE
import com.quizle.data.utils.Constant.USER_PASSWORD_PARAMETER
import com.quizle.data.utils.Constant.USER_REGISTER_ROUTE
import com.quizle.data.utils.Constant.USER_ROUTE
import com.quizle.data.utils.Constant.USER_TOKEN_EXP_PARAMETER
import com.quizle.data.utils.FileReader
import com.quizle.data.utils.ServerDataError
import com.quizle.data.utils.retryNetworkRequest
import com.quizle.domain.utils.Result
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders


class KtorUserRemoteDataSource(
    private val httpClient: HttpClient,
    private val fileReader: FileReader,
    private val appPreferences: AppPreferences
): RemoteUserDataSource {

    private val language = appPreferences.loadSettings().language
    override suspend fun getUserById(id: String): Result<UserDto, ServerDataError> {
        return safeCall<UserDto> {
            retryNetworkRequest {
                httpClient.get(urlString = "$USER_ROUTE/$id"){
                    header(HttpHeaders.AcceptLanguage, language)
                }
            }
        }
    }



    override suspend fun updateUser(user: UserDto): Result<String, ServerDataError> {
        return safeCall<String> {
            retryNetworkRequest {
                httpClient.patch(urlString = USER_UPDATE_ROUTE){
                    header(HttpHeaders.AcceptLanguage, language)
                    setBody(user)
                }
            }
        }
    }


    override suspend fun login(email: String, password: String, tokenExp: Long?): Result<UserDto, ServerDataError> {
        return safeCall<UserDto> {
            retryNetworkRequest {
                httpClient.post(urlString = USER_LOGIN_ROUTE){
                    header(HttpHeaders.AcceptLanguage, language)
                    parameter(key = USER_EMAIL_PARAMETER, value = email)
                    parameter(key = USER_PASSWORD_PARAMETER, value = password)
                    parameter(key = USER_TOKEN_EXP_PARAMETER, value = tokenExp)
                }
            }
        }
    }



    override suspend fun register(user: UserDto): Result<UserDto, ServerDataError> {
        return safeCall<UserDto> {
            retryNetworkRequest {
                httpClient.post(urlString = USER_REGISTER_ROUTE){
                    header(HttpHeaders.AcceptLanguage, language)
                    setBody(user)
                }
            }
        }
    }


    override suspend fun logEvent(email: String, action: String): Result<LogEventDto, ServerDataError> {
        return safeCall<LogEventDto> {
            retryNetworkRequest {
                httpClient.post(urlString = LOG_USER_ACTIVITY_ROUTE){
                    header(HttpHeaders.AcceptLanguage, language)
                    parameter(USER_EMAIL_PARAMETER, email)
                    parameter(USER_ACTION_PARAMETER, action)
//                    setBody(activity)
                }
            }
        }
    }

    override suspend fun logout(userId: String): Result<String, ServerDataError> {
        return safeCall<String> {
            retryNetworkRequest {
                httpClient.post(urlString = USER_LOGOUT_ROUTE) {
                    header(HttpHeaders.AcceptLanguage, language)
                    parameter(USER_ID_PARAMETER, userId)
                }
            }
        }
    }



    override suspend fun upsertImageProfile(image: Uri): Result<String, ServerDataError> {
        return safeCall<String> {
            retryNetworkRequest {
                val info = fileReader.uriToFileInfo(image)
                httpClient.submitFormWithBinaryData(
                    url = USER_IMAGE_PROFILE_ADD_ROUTE ,
                    formData = formData {
                        append(
                            key = HttpHeaders.AcceptLanguage,
                            value = language
                        )
                        append("description", "Test")
                        append("the_file", info.bytes, Headers.build {
                            append(
                                name = HttpHeaders.ContentType,
                                value = info.mimeType)
                            append(
                                name = HttpHeaders.ContentDisposition,
                                value = "filename=${info.name}"
                            )
                        })

                    }
                ) {
                    onUpload { bytesSentTotal, totalBytes ->
//                        if(totalBytes > 0L) {
//                            send(ProgressUpdate(bytesSentTotal, totalBytes))
//                        }
                    }
                }
            }
        }
    }

    override suspend fun deleteImageProfile(imageUri: String): Result<String, ServerDataError> {
        return safeCall<String> {
            retryNetworkRequest {
                httpClient.delete(urlString = USER_IMAGE_PROFILE_DELETE_ROUTE){
                    header(HttpHeaders.AcceptLanguage, language)
                    parameter(IMAGE_URL_PARAMETER, imageUri)
                }
            }
        }
    }

    override suspend fun downloadImageProfile(imageUri: String): Result<ByteArray, ServerDataError> {
        return safeCall<ByteArray> {
            retryNetworkRequest {
                httpClient.get(urlString = USER_IMAGE_PROFILE_ROUTE){
                    header(HttpHeaders.AcceptLanguage, language)
                    parameter(IMAGE_URL_PARAMETER, imageUri)
                }
            }
        }
    }


}