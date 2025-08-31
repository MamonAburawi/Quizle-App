package com.quizle.data.respository

import com.quizle.data.mapper.toAppRelease
import com.quizle.data.remote.data_source.app_info.KtorAppInfoDataSource
import com.quizle.data.remote.dto.AppReleaseDto
import com.quizle.domain.module.AppRelease
import com.quizle.domain.repository.AppReleaseInfoRepository
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result
import com.quizle.domain.util.Result.*

class AppReleaseInfoRepositoryImpl(
    private val remoteDataSource: KtorAppInfoDataSource
): AppReleaseInfoRepository {

    override suspend fun getAppReleaseInfo(): Result<AppRelease, DataError> {
        return try {
            when(val response = remoteDataSource.getLastAppReleaseInfo() ){
                is Success -> {
                    val data = response.data.toAppRelease()
                    Success(data)
                }
                is Failure -> {
                    Failure(response.error)
                }
                is SuccessMessage -> { // because here in this cause we receive only data class not string
                    Failure(DataError.ConflictDataType)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(DataError.Unknown)
        }
    }

}