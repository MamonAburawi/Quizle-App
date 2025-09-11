package com.quizle.presentation.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.data.utils.LogEvent
import com.quizle.domain.repository.UserRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.util.Validator
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(
    private val userRepository: UserRepository,
    private val validator: Validator
): ViewModel(){

    companion object{
        private const val THREE_MONTHS_IN_MILLIS = 7_776_000_000L
    }
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()



    fun onAction(action: LoginAction){
        when(action){
            is LoginAction.EmailChanged -> {
                _state.update { it.copy(email = action.email, emailFieldErrorMessage = null) }
            }
            is LoginAction.PasswordChanged -> {
                _state.update { it.copy(password = action.password, passwordFieldErrorMessage = null) }
            }
            is LoginAction.RememberMeChanged -> {
                _state.update { it.copy(rememberMe = action.rememberMe) }
            }
            is LoginAction.LoginButtonClicked -> {
                login()
            }
            is LoginAction.SignUpButtonClicked -> {
                _event.trySend(LoginEvent.NavigateToSignUpScreen)
            }

            LoginAction.ResetPasswordButtonClicked -> {
                _state.update { it.copy(showResetPasswordNavigationBottom = true) }
            }

            LoginAction.ResetPasswordDialogConfirm ->{
                resetPassword()
            }
            LoginAction.ResetPasswordDialogDismiss -> {
                _state.update { it.copy(showResetPasswordNavigationBottom = false) }
            }
        }

    }


    private fun resetPassword() {
        viewModelScope.launch {

            _state.update { it.copy(showResetPasswordNavigationBottom = false) }

        }
    }

    private fun login() {
        viewModelScope.launch {
            val email = _state.value.email.trim()
            Log.d("LoginViewModel", "email: $email")
            val password = _state.value.password.trim()
            val emailError = validator.validateEmail(email)
            val passwordError = validator.validatePassword(password)
            val tokenExpAfter3Months = if (_state.value.rememberMe) System.currentTimeMillis() + THREE_MONTHS_IN_MILLIS else null
            if (emailError != null){
                _state.update { it.copy(emailFieldErrorMessage = emailError) }
            }
            if (passwordError != null){
                _state.update { it.copy(passwordFieldErrorMessage = passwordError) }
            }

            if (validator.isAllFieldValidate()){
                _state.value = _state.value.copy(isLoading = true, error = null)

                userRepository.login(email = email, password = password, tokenExp = tokenExpAfter3Months)
                    .onSuccess(
                        onDataSuccess = { user ->
                            async { userRepository.logEvent(LogEvent.LOGIN_EVENT)}.await()
                            _event.trySend(LoginEvent.NavigateToDashboardScreen)
                            _state.value = _state.value.copy(isLoading = false)
                        }
                    )
                    .onFailure {
                        val error = it.getErrorMessage()
                        _event.send(LoginEvent.ShowToastMessage(message = error, type = MessageType.Error))
                        _state.value = _state.value.copy(isLoading = false)
                    }
            }

        }
    }





}





