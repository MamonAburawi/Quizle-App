package com.quizle.data.mapper

import com.quizle.data.remote.dto.UserActivityDto
import com.quizle.domain.module.UserEvent

fun UserActivityDto.toUserActivity(): UserEvent{
    return UserEvent(
        id = id,
        userName = userName,
        createdAt = createdAt,
        action = action,
        userId = userId
    )
}


fun UserEvent.toUserActivityDto(): UserActivityDto{
    return UserActivityDto(
        id = id,
        userName = userName,
        createdAt = createdAt,
        action = action,
        userId = userId
    )
}