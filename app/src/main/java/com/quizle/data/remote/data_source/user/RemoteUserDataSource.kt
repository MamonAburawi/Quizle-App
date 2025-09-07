package com.quizle.data.remote.data_source.user


import android.net.Uri
import com.quizle.data.remote.dto.UserActivityDto
import com.quizle.data.remote.dto.UserDto
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result

interface RemoteUserDataSource {

    suspend fun getUserById(id: String): Result<UserDto?, ServerDataError>

    suspend fun updateUser(user: UserDto): Result<String, ServerDataError>

    suspend fun login(email: String, password: String, tokenExp: Long?): Result<UserDto, ServerDataError>

    suspend fun register(user: UserDto): Result<UserDto, ServerDataError>

    suspend fun logUserActivity(activity: UserActivityDto): Result<UserActivityDto, ServerDataError>

    suspend fun logout(userId: String): Result<String, ServerDataError>

    suspend fun upsertImageProfile(image: Uri): Result<String,ServerDataError>

    suspend fun deleteImageProfile(imageUri: String): Result<String, ServerDataError>

    suspend fun downloadImageProfile(imageUri: String): Result<ByteArray,ServerDataError>



}