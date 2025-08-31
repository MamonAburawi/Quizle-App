package com.quizle.presentation.screens.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,

    val emailFieldErrorMessage: String? = null,
    val passwordFieldErrorMessage: String? = null,
    val error: String? = null,
    val successMessage: String? = null,

    val showResetPasswordNavigationBottom: Boolean = false
)
