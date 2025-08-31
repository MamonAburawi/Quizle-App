package com.quizle.presentation.screens.sign_up



sealed interface SignUpAction {
    data class EmailChanged(val email: String) : SignUpAction
    data class PasswordChanged(val password: String) : SignUpAction
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpAction
    data class NameChanged(val name: String) : SignUpAction
    data object SignUpButtonClicked : SignUpAction
    data object LoginButtonClicked : SignUpAction
}