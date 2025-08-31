package com.quizle.domain.repository



import com.quizle.domain.module.AppRelease
import com.quizle.domain.util.DataError

import com.quizle.domain.util.Result

interface AppReleaseInfoRepository {

    suspend fun getAppReleaseInfo(): Result<AppRelease, DataError>
}