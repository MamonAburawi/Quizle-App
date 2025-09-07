package com.quizle.presentation.screens.sign_up

data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,

    val nameFieldErrorMessage: String? = null,
    val emailFieldErrorMessage: String? = null,
    val passwordFieldErrorMessage: String? = null,
    val confirmPasswordErrorMessage: String? = null,
    val error: String? = null
)
