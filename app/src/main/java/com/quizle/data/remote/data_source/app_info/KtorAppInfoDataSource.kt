package com.quizle.data.remote.data_source.app_info

import com.quizle.data.remote.dto.AppReleaseDto
import com.quizle.data.remote.safeCall
import com.quizle.data.util.Constant.LAST_RELEASE_ROUTE
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorAppInfoDataSource(
    private val httpClient: HttpClient
): RemoteAppInfoDataSource {

    override suspend fun getLastAppReleaseInfo(): Result<AppReleaseDto, DataError> {
        return safeCall<AppReleaseDto> {
            httpClient.get(urlString = LAST_RELEASE_ROUTE)
        }
    }

}