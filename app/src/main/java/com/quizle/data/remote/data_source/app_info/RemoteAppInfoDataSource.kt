package com.quizle.data.remote.data_source.app_info

import com.quizle.data.remote.dto.AppReleaseDto
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result

interface RemoteAppInfoDataSource {

    suspend fun getLastAppReleaseInfo(): Result<AppReleaseDto, ServerDataError>

}