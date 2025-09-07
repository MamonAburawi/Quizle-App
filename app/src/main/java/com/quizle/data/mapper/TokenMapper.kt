package com.quizle.data.mapper

import com.quizle.data.local.entity.UserEntity
import com.quizle.data.remote.dto.UserDto
import com.quizle.domain.module.User.Token

fun Token.toTokenDto(): UserDto.Token {
    return UserDto.Token(
        accessToken = accessToken,
        expAt = expAt,
        createdAt = createdAt,
        type = type
    )
}


fun UserDto.Token.toToken(): Token {
    return Token(
        accessToken = accessToken,
        expAt = expAt,
        createdAt = createdAt,
        type = type
    )
}

fun UserDto.Token.toTokenEntity(): UserEntity.Token {
    return UserEntity.Token(
        accessToken = accessToken,
        expAt = expAt,
        createdAt = createdAt,
        type = type
    )
}

fun UserEntity.Token.toToken(): Token {
    return Token(
        accessToken = accessToken,
        expAt = expAt,
        createdAt = createdAt,
        type = type
    )
}