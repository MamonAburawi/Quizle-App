package com.quizle.data.remote.data_source.app_info

import com.quizle.data.remote.dto.AppReleaseDto
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result

interface RemoteAppInfoDataSource {

    suspend fun getLastAppReleaseInfo(): Result<AppReleaseDto, DataError>

}