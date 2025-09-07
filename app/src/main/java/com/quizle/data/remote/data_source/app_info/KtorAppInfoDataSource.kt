package com.quizle.data.remote.data_source.app_info

import com.quizle.data.remote.dto.AppReleaseDto
import com.quizle.data.utils.safeCall
import com.quizle.data.utils.Constant.LAST_RELEASE_ROUTE
import com.quizle.data.utils.ServerDataError
import com.quizle.data.utils.retryNetworkRequest
import com.quizle.domain.utils.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorAppInfoDataSource(
    private val httpClient: HttpClient
): RemoteAppInfoDataSource {

    override suspend fun getLastAppReleaseInfo(): Result<AppReleaseDto, ServerDataError> {
        return safeCall<AppReleaseDto> {
            retryNetworkRequest {
                httpClient.get(urlString = LAST_RELEASE_ROUTE)
            }
        }
    }

}