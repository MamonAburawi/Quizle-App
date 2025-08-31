package com.quizle.data.mapper


import com.quizle.data.local.entity.UserEntity
import com.quizle.data.remote.dto.UserDto
import com.quizle.domain.module.User



fun User.Settings.toSettingsDto(): UserDto.Settings {
    return UserDto.Settings(
        enableNotificationApp = enableNotificationApp,
        enableQuizTime = enableQuizTime,
        switchToCustomTimeInMin = switchToCustomTimeInMin,
        customQuizTimeInMin = customQuizTimeInMin

    )
}


fun UserDto.Settings.dtoToSettings(): User.Settings {
    return User.Settings(
        enableNotificationApp = enableNotificationApp,
        enableQuizTime = enableQuizTime,
        switchToCustomTimeInMin = switchToCustomTimeInMin,
        customQuizTimeInMin = customQuizTimeInMin
    )
}

fun UserEntity.Settings.entityToSettings(): User.Settings {
    return User.Settings(
        enableNotificationApp = enableNotificationApp,
        enableQuizTime = enableQuizTime,
        switchToCustomTimeInMin = switchToCustomTimeInMin,
        customQuizTimeInMin = customQuizTimeInMin
    )
}

fun UserDto.Settings.toSettingsEntity(): UserEntity.Settings {
    return UserEntity.Settings(
        enableNotificationApp = enableNotificationApp,
        enableQuizTime = enableQuizTime,
        switchToCustomTimeInMin = switchToCustomTimeInMin,
        customQuizTimeInMin = customQuizTimeInMin
    )
}

