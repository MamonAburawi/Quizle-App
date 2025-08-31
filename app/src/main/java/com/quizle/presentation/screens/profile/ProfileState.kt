package com.quizle.presentation.screens.profile

import com.quizle.domain.module.User

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User = User(),
    val error: String? = null,
    val isImageProfileLoading: Boolean = false,

    val fullNameError: String? = null,
)
