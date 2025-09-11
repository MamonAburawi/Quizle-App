package com.quizle.data.respository

import android.net.Uri
import com.quizle.data.local.dao.UserDao
import com.quizle.data.local.prefrences.AppPreferences
import com.quizle.data.mapper.toUser
import com.quizle.data.mapper.toUserDto
import com.quizle.data.mapper.toUserEntity
import com.quizle.data.remote.data_source.user.KtorUserRemoteDataSource
import com.quizle.domain.module.AppSettings
import com.quizle.domain.module.User
import com.quizle.domain.repository.UserRepository
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result
import com.quizle.domain.utils.Result.*
import kotlinx.coroutines.runBlocking


class UserRepositoryImpl(
    private val remoteDataSource: KtorUserRemoteDataSource,
    private val localDataSource: UserDao,
    private val appPreferences: AppPreferences
): UserRepository {


    override suspend fun login(email: String, password: String, tokenExp: Long?): Result<User, ServerDataError> {
        return try {
            when (val result = remoteDataSource.login(email, password, tokenExp)){
                is Success -> {
                    val userDto = result.data
                    localDataSource.insertUser(userDto.toUserEntity())
                    appPreferences.storeUserId(userId = userDto.id)
                    appPreferences.storeSettings(
                        isEnableNotificationApp = userDto.settings.enableNotificationApp,
                        isEnableQuizTimeInMin = userDto.settings.enableQuizTime,
                        isEnableCustomTimeSwitch = userDto.settings.switchToCustomTimeInMin,
                        customQuizTimeInMin = userDto.settings.customQuizTimeInMin,
                        language = userDto.language
                    )
                    Success(userDto.toUser())
                }
                is Failure -> {
                    Failure(result.error)
                }

                is SuccessMessage -> {
                    Failure(ServerDataError.ConflictServerDataType)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }


    override suspend fun register(user: User): Result<User, ServerDataError> {
        return try {
            when (val result = remoteDataSource.register(user.toUserDto())){
                is Success -> {
                    val userDto = result.data
                    localDataSource.insertUser(userDto.toUserEntity())
                    appPreferences.storeUserId(userId = userDto.id)
                    appPreferences.storeSettings(
                        isEnableNotificationApp = user.settings.enableNotificationApp,
                        isEnableQuizTimeInMin = user.settings.enableQuizTime,
                        isEnableCustomTimeSwitch = user.settings.switchToCustomTimeInMin,
                        customQuizTimeInMin = user.settings.customQuizTimeInMin,
                        language = user.language
                    )
                    Success(userDto.toUser())
                }
                is Failure ->  Failure(result.error)
                is SuccessMessage ->  Failure(ServerDataError.ConflictServerDataType)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }


    override suspend fun logEvent(logEvent: String): Result<String, ServerDataError> {
        return try {
            val userId = appPreferences.getUserId()
            val localUser = localDataSource.getUserById(userId)

            if (localUser == null) {
                throw Exception("User not found in local storage")
            }

//            val logActivity = UserEvent(
//                action = logEvent,
//                createdAt = System.currentTimeMillis(),
//                userName = localUser.userName,
//                userId = userId
//            )

            val result = remoteDataSource.logEvent(action = logEvent, email = localUser.email)

            // This is a cleaner way to handle the result
            when (result) {
                is SuccessMessage -> Success(result.message)
                is Failure -> Failure(result.error)
                is Success ->  Failure(ServerDataError.ConflictServerDataType)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }

    override suspend fun upsertImageProfile(image: Uri): Result<String, ServerDataError> {
        return try {
            when(val result = remoteDataSource.upsertImageProfile(image)){
                is Success -> Failure(ServerDataError.ConflictServerDataType)
                is SuccessMessage -> SuccessMessage(message = result.message)
                is Failure -> Failure(result.error)
            }
        }
        catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }

    override suspend fun deleteImageProfile(imageUrl: String): Result<String, ServerDataError> {
        return try {
            when(val result = remoteDataSource.deleteImageProfile(imageUrl)){
                is Failure -> Failure(result.error)
                is SuccessMessage -> SuccessMessage(result.message)
                is Success -> Failure(ServerDataError.ConflictServerDataType)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }

    override suspend fun downloadImageProfile(imageUrl: String): Result<ByteArray, ServerDataError> {
        return try {
            when(val result = remoteDataSource.downloadImageProfile(imageUrl)){
                is Success-> Success(result.data)
                is Failure -> Failure(result.error)
                is SuccessMessage -> Failure(ServerDataError.ConflictServerDataType)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }

    suspend fun clearLocalData() {
        appPreferences.clearAllPreferences()
        localDataSource.clearAllUsers()
    }


    override suspend fun loadUser(): Result<User, ServerDataError> {
        return try {
            val userId = appPreferences.getUserId()

            if (userId.isEmpty()) {
                throw Exception("User ID not found in shared preferences")
            }

            when (val result = remoteDataSource.getUserById(userId)) {
                is Success -> {
                    val userDto = result.data
                    localDataSource.insertUser(userDto.toUserEntity())
                    Success(userDto.toUser())
                }
                is Failure -> {
                    val userId = appPreferences.getUserId()
                    val userEntity = localDataSource.getUserById(userId)
                    return userEntity?.let {
                        Success(it.toUser())
                    } ?: Failure(result.error)
                }

                is SuccessMessage -> Failure(ServerDataError.ConflictServerDataType)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }


    suspend fun updateUserSettings(
        enableNotificationApp: Boolean,
        enableQuizTime: Boolean,
        switchToCustomTimeInMin: Boolean,
        customQuizTimeInMin: Int,
        language: String
    ): Result<String, ServerDataError> {
        return try {
            val userId = appPreferences.getUserId()
            val userEntity = localDataSource.getUserById(userId)

            if (userEntity == null) {
                throw Exception("User not found in local storage")
            }

            val updatedUserEntity = userEntity.copy(
                language = language,
                settings = userEntity.settings.copy(
                    enableNotificationApp = enableNotificationApp,
                    switchToCustomTimeInMin = switchToCustomTimeInMin,
                    enableQuizTime = enableQuizTime,
                    customQuizTimeInMin = customQuizTimeInMin,
                )
            )

            appPreferences.storeSettings(
                isEnableNotificationApp = enableNotificationApp,
                isEnableQuizTimeInMin = enableQuizTime,
                isEnableCustomTimeSwitch = switchToCustomTimeInMin,
                customQuizTimeInMin = customQuizTimeInMin,
                language = language
            )

            when (val result = updateUser(updatedUserEntity.toUser())) {
                is Success -> Failure(ServerDataError.ConflictServerDataType)
                is Failure -> Failure(result.error)
                is SuccessMessage -> {
                    localDataSource.insertUser(updatedUserEntity)
                    SuccessMessage(result.message)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }

    fun loadSettings(): AppSettings {
        return runBlocking { // This blocks the current thread until the coroutine inside completes
            try {
              appPreferences.loadSettings()
            } catch (ex: Exception) {
                ex.printStackTrace()
                AppSettings()
            }
        }
    }

    override suspend fun logout(): Result<String, ServerDataError> {
      return try {
          val userId = appPreferences.getUserId()
          when(val result = remoteDataSource.logout(userId)){
              is Success -> Failure(ServerDataError.ConflictServerDataType)
              is Failure -> Failure(result.error)
              is SuccessMessage -> SuccessMessage(result.message)
          }
      }catch (ex: Exception){
          ex.printStackTrace()
          Failure(ServerDataError.Unknown)
        }
    }




    override suspend fun resetPassword(email: String, password: String): Result<String, ServerDataError> {
        TODO("Not yet implemented")
    }



    override suspend fun updateUser(user: User): Result<String, ServerDataError> {
        return try {
            val userDto = user.toUserDto()
            when(val result = remoteDataSource.updateUser(userDto)){
                is Success -> Failure(ServerDataError.ConflictServerDataType)
                is Failure ->  Failure(result.error)
                is SuccessMessage -> {
                    localDataSource.insertUser(userDto.toUserEntity())
                    appPreferences.storeSettings(
                        isEnableNotificationApp = user.settings.enableNotificationApp,
                        isEnableQuizTimeInMin = user.settings.enableQuizTime,
                        isEnableCustomTimeSwitch = user.settings.switchToCustomTimeInMin,
                        customQuizTimeInMin = user.settings.customQuizTimeInMin,
                        language = user.language
                    )
                    SuccessMessage(result.message)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }




}