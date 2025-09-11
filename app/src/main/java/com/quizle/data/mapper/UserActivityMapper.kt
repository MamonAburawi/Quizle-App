package com.quizle.data.mapper

import com.quizle.data.remote.dto.LogEventDto
import com.quizle.domain.module.UserEvent

fun LogEventDto.toUserActivity(): UserEvent{
    return UserEvent(
        id = id,
        userName = userName,
        createdAt = createdAt,
        action = action,
        userId = userId
    )
}


fun UserEvent.toUserActivityDto(): LogEventDto{
    return LogEventDto(
        id = id,
        userName = userName,
        createdAt = createdAt,
        action = action,
        userId = userId
    )
}