package com.quizle.presentation.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.data.respository.UserRepositoryImpl
import com.quizle.domain.util.onFailure
import com.quizle.domain.util.onSuccess
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepositoryImpl,
): ViewModel() {


    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _event = Channel<ProfileEvent>()
    val event = _event.receiveAsFlow()


    init {
        loadUserData()
    }


    fun onAction(action: ProfileAction){
        when(action){
            is ProfileAction.GenderButtonClicked -> {
                val gender = action.gender
                Log.d("ProfileViewModel","gender : $gender")
                _state.update { it.copy(user = it.user.copy(gender = gender)) }
            }
            is ProfileAction.SaveChangeButtonClicked -> {
                updateUser()
            }

            is ProfileAction.UploadImageButtonClicked -> {
                val imageUri = action.uri
                upsertImageProfile(imageUri)

            }

            is ProfileAction.OnFullNameTextChanged -> {
                val fullName = action.fullName
                _state.update { it.copy(user = it.user.copy(userName = fullName)) }
            }

            is ProfileAction.DeleteImageProfileButtonClicked ->{
                val imageUrl = action.imageUrl
                deleteImageProfile(imageUrl)

            }
        }
    }

    private fun deleteImageProfile(imageUrl: String) {
        viewModelScope.launch {
                _state.update { it.copy(isImageProfileLoading = true) }
                userRepository.deleteImageProfile(imageUrl)
                    .onSuccess {
                        _state.update { it.copy(isImageProfileLoading = false, user = it.user.copy(imageProfile = null)) }
                    }
                    .onFailure { error ->
                        _state.update { it.copy(isImageProfileLoading = false) }
                        _event.send(ProfileEvent.ShowMessage(error.getErrorMessage(), MessageType.Error))
                        Log.e("ProfileViewModel","failed to delete image profile : ${error.getErrorMessage()}")
                    }

        }
    }

    private fun upsertImageProfile(image: Uri?) {
        viewModelScope.launch {
            if (image != null){
                _state.update { it.copy(isImageProfileLoading = true) }
                    userRepository.upsertImageProfile(image)
                        .onSuccess(
                            onMessageSuccess = { fullPath ->
                                _state.update { it.copy(isImageProfileLoading = false, user = it.user.copy(imageProfile = fullPath)) }
                            }
                        )
                        .onFailure { error ->
                            _state.update { it.copy(isImageProfileLoading = false) }
                            _event.send(ProfileEvent.ShowMessage(error.getErrorMessage(), MessageType.Error))
                        }
            }
        }
    }

    private fun updateUser(){
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val currentUser = _state.value.user

            userRepository.updateUser(currentUser)
                .onSuccess(
                    onMessageSuccess = { message ->
                        _state.update { it.copy(isLoading = false) }
                        _event.send(ProfileEvent.ShowMessage(message = message, type = MessageType.Success))
                        delay(500)
                        _event.send(ProfileEvent.NavigateToHomeScreen)
                    }
                )
                .onFailure { error ->
                    _event.send(ProfileEvent.ShowMessage(error.getErrorMessage(), MessageType.Error))
                    _state.update { it.copy(isLoading = false, error = error.getErrorMessage()) }
                    Log.e("ProfileViewModel","failed to update : ${error.getErrorMessage()}")
                }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, fullNameError = null) }
            userRepository.loadUser()
                .onSuccess(
                    onDataSuccess = {user ->
                        _state.update { it.copy(isLoading = false, user = user) }
                    }
                )
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    _event.send(ProfileEvent.ShowMessage(message = error.getErrorMessage(), type = MessageType.Error))
                }
        }
    }







}