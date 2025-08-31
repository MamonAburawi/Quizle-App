package com.quizle.presentation.screens.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.domain.module.User
import com.quizle.domain.repository.UserRepository
import com.quizle.domain.util.onFailure
import com.quizle.domain.util.onSuccess
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.util.RecordUserEvent
import com.quizle.presentation.util.Validator
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository,
    private val validator: Validator
): ViewModel(){


    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _event = Channel<SignUpEvent>()
    val event = _event.receiveAsFlow()


    fun onAction(action: SignUpAction){
        when(action){
            is SignUpAction.NameChanged -> {
                _state.value = _state.value.copy(
                    name = action.name,
                    nameFieldErrorMessage = null
                )
            }
            is SignUpAction.EmailChanged -> {
                _state.value = _state.value.copy(
                    email = action.email,
                    emailFieldErrorMessage = null
                )
            }

            is SignUpAction.PasswordChanged -> {
                _state.value = _state.value.copy(
                    password = action.password,
                    passwordFieldErrorMessage = null
                )
            }
            is SignUpAction.ConfirmPasswordChanged -> {
                _state.value = _state.value.copy(
                    confirmPassword = action.confirmPassword,
                    confirmPasswordErrorMessage = null
                )
            }

            is SignUpAction.LoginButtonClicked -> {
                _event.trySend(SignUpEvent.NavigateToLoginScreen)
            }

            is SignUpAction.SignUpButtonClicked -> {
                register()
            }

        }
    }

    private fun register(){
        viewModelScope.launch {
            val name = _state.value.name
            val email = _state.value.email
            val password = _state.value.password
            val confirmPassword = _state.value.confirmPassword

            val nameError = validator.validateUserName(name)
            val emailError = validator.validateEmail(email)
            val passwordError = validator.validatePassword(password)
            val confirmPasswordError = validator.validateConfirmPassword(confirmPassword, password)

            if (nameError != null){
                _state.value = _state.value.copy(nameFieldErrorMessage = nameError)
            }
            if (emailError != null){
                _state.value = _state.value.copy(emailFieldErrorMessage = emailError)
            }
            if (passwordError != null){
                _state.value = _state.value.copy(passwordFieldErrorMessage = passwordError)
            }
            if (confirmPasswordError != null){
                _state.value = _state.value.copy(confirmPasswordErrorMessage = confirmPasswordError)
            }

            if (validator.isAllFieldValidate()){
                _state.value = _state.value.copy(
                    isLoading = true,
                    error = null
                )
                val user = User(
                    gender = "male",
                    userName = name,
                    email = email,
                    password = password
                )

                userRepository.register(user)
                    .onSuccess(
                        onDataSuccess = {
                            async { userRepository.recordUserEvent(RecordUserEvent.REGISTER) }.await()
                            _event.trySend(SignUpEvent.NavigateToDashboardScreen)
                            _state.value = _state.value.copy(isLoading = false)
                        }
                    )
                    .onFailure {
                        val error = it.getErrorMessage()
                        _event.send(SignUpEvent.ShowToastMessage(message = error, type = MessageType.Error))
                        _state.value = _state.value.copy(isLoading = false)
                    }
            }

        }

    }



    private fun resetError(){
        _state.value = _state.value.copy(
            emailFieldErrorMessage = null, passwordFieldErrorMessage = null, error = null
        )
    }





}