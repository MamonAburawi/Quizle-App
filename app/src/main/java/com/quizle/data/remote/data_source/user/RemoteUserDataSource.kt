package com.quizle.data.remote.data_source.user


import android.net.Uri
import com.quizle.data.remote.dto.UserActivityDto
import com.quizle.data.remote.dto.UserDto
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result

interface RemoteUserDataSource {

    suspend fun getUserById(id: String): Result<UserDto?, DataError>

    suspend fun updateUser(user: UserDto): Result<String, DataError>

    suspend fun login(email: String, password: String, tokenExp: Long?): Result<UserDto, DataError>

    suspend fun register(user: UserDto): Result<UserDto, DataError>

    suspend fun logUserActivity(activity: UserActivityDto): Result<UserActivityDto, DataError>

    suspend fun logout(userId: String): Result<String, DataError>

    suspend fun upsertImageProfile(image: Uri): Result<String,DataError>

    suspend fun deleteImageProfile(imageUri: String): Result<String, DataError>

    suspend fun downloadImageProfile(imageUri: String): Result<ByteArray,DataError>



}