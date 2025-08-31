package com.quizle.domain.repository


import android.net.Uri
import com.quizle.domain.module.User
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result
import com.quizle.presentation.util.RecordUserEvent

interface UserRepository {

    suspend fun login(email: String, password: String, tokenExp: Long? = null): Result<User,DataError>

    suspend fun register(user: User): Result<User,DataError>

    suspend fun resetPassword(email: String, password: String): Result<String,DataError>

    suspend fun loadUser(): Result<User,DataError>

    suspend fun logout(): Result<String,DataError>

    suspend fun updateUser(user: User): Result<String,DataError>

//    suspend fun loadUserToken(): User.Token

    suspend fun recordUserEvent(log: RecordUserEvent): Result<String, DataError>


    suspend fun upsertImageProfile(image: Uri): Result<String,DataError>

    suspend fun deleteImageProfile(imageUrl: String): Result<String,DataError>

    suspend fun downloadImageProfile(imageUrl: String): Result<ByteArray,DataError>




}