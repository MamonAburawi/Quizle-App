package com.quizle.domain.repository


import android.net.Uri
import com.quizle.domain.module.User
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result


interface UserRepository {

    suspend fun login(email: String, password: String, tokenExp: Long? = null): Result<User,ServerDataError>

    suspend fun register(user: User): Result<User,ServerDataError>

    suspend fun resetPassword(email: String, password: String): Result<String,ServerDataError>

    suspend fun loadUser(): Result<User,ServerDataError>

    suspend fun logout(): Result<String,ServerDataError>

    suspend fun updateUser(user: User): Result<String,ServerDataError>

//    suspend fun loadUserToken(): User.Token

    suspend fun logEvent(logEvent: String): Result<String, ServerDataError>


    suspend fun upsertImageProfile(image: Uri): Result<String,ServerDataError>

    suspend fun deleteImageProfile(imageUrl: String): Result<String,ServerDataError>

    suspend fun downloadImageProfile(imageUrl: String): Result<ByteArray,ServerDataError>




}