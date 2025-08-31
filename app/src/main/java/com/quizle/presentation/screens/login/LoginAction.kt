package com.quizle.presentation.screens.login

sealed interface LoginAction {
    data class EmailChanged(val email: String) : LoginAction
    data class PasswordChanged(val password: String) : LoginAction
    data class RememberMeChanged(val rememberMe: Boolean) : LoginAction
    data object LoginButtonClicked : LoginAction
    data object SignUpButtonClicked : LoginAction

    data object ResetPasswordButtonClicked : LoginAction
    data object ResetPasswordDialogDismiss : LoginAction
    data object ResetPasswordDialogConfirm : LoginAction


}