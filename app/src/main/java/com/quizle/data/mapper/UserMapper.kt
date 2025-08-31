package com.quizle.data.mapper

import com.quizle.data.local.entity.UserEntity
import com.quizle.data.remote.dto.UserDto
import com.quizle.domain.module.User


fun UserDto.toUser(): User {
    return User(
        id = id,
        userName = userName,
        email = email,
        password = password,
        phone = phone,
        token = token.toToken(),
        gender = gender,
        accountType = accountType,
        imageProfile = imageProfile,
        favoriteTopicsIds = favoriteTopicsIds,
        likedTopicsIds = likedTopicsIds,
        disLikedTopicsIds = disLikedTopicsIds,
        resultQuizziesIds = resultQuizziesIds,
        timeSpentQuizzingInMin = timeSpentQuizzingInMin,
        totalCorrectAnswers = totalCorrectAnswers,
        totalQuizzes = totalQuizzes,
        countryCode = countryCode,
        language = language,
        isActive = isActive,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        settings = settings.dtoToSettings(),
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        userName = userName,
        email = email,
        password = password,
        phone = phone,
        token = token.toTokenDto(),
        gender = gender,
        accountType = accountType,
        imageProfile = imageProfile,
        favoriteTopicsIds = favoriteTopicsIds,
        likedTopicsIds = likedTopicsIds,
        disLikedTopicsIds = disLikedTopicsIds,
        resultQuizziesIds = resultQuizziesIds,
        timeSpentQuizzingInMin = timeSpentQuizzingInMin,
        totalCorrectAnswers = totalCorrectAnswers,
        totalQuizzes = totalQuizzes,
        countryCode = countryCode,
        language = language,
        isActive = isActive,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        settings = settings.toSettingsDto()
    )
}

fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        userName = userName,
        email = email,
        password = password,
        phone = phone,
        token = token.toTokenEntity(),
        gender = gender,
        accountType = accountType,
        imageProfile = imageProfile,
        favoriteTopicsIds = favoriteTopicsIds,
        likedTopicsIds = likedTopicsIds,
        disLikedTopicsIds = disLikedTopicsIds,
        resultQuizziesIds = resultQuizziesIds,
        timeSpentQuizzingInMin = timeSpentQuizzingInMin,
        totalCorrectAnswers = totalCorrectAnswers,
        totalQuizzes = totalQuizzes,
        countryCode = countryCode,
        language = language,
        isActive = isActive,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        settings = settings.toSettingsEntity()
    )
}

fun UserEntity.toUser(): User {
    return User(
        id = id,
        userName = userName,
        email = email,
        password = password,
        phone = phone,
        token = token.toToken(),
        gender = gender,
        accountType = accountType,
        imageProfile = imageProfile,
        favoriteTopicsIds = favoriteTopicsIds,
        likedTopicsIds = likedTopicsIds,
        disLikedTopicsIds = disLikedTopicsIds,
        resultQuizziesIds = resultQuizziesIds,
        timeSpentQuizzingInMin = timeSpentQuizzingInMin,
        totalCorrectAnswers = totalCorrectAnswers,
        totalQuizzes = totalQuizzes,
        countryCode = countryCode,
        language = language,
        isActive = isActive,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        settings = settings.entityToSettings()
    )
}

