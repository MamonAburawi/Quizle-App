package com.quizle.domain.repository



import com.quizle.domain.module.AppRelease
import com.quizle.data.utils.ServerDataError

import com.quizle.domain.utils.Result

interface AppReleaseInfoRepository {

    suspend fun getAppReleaseInfo(): Result<AppRelease, ServerDataError>
}