package com.quizle.presentation.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.R
import com.quizle.data.respository.UserRepositoryImpl
import com.quizle.data.utils.LogEvent
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.util.StringProvider
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val userRepository: UserRepositoryImpl,
    private val stringProvider: StringProvider
): ViewModel() {


    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _event = Channel<SettingsEvent>()
    val event = _event.receiveAsFlow()


    init {
        initSettings()
    }

    fun onAction(action: SettingsAction){
        when(action){
            is SettingsAction.CustomTimeChanged -> {
                val newTimeInMin  = action.minutes
                _state.update { it.copy(customTimeInMinutes = newTimeInMin) }
            }
            is SettingsAction.EnableCustomTimeButtonClicked -> {
                _state.update { it.copy(isEnableCustomTimeSwitch = !it.isEnableCustomTimeSwitch) }
            }
            is SettingsAction.EnableQuizTimeButtonClicked -> {
                _state.update { it.copy(isQuizTimeEnabled = !it.isQuizTimeEnabled) }
            }
            is SettingsAction.LanguageButtonClicked -> {
                val newLanguage = action.language
                _state.update { it.copy(selectedLanguage = newLanguage) }
            }
            is SettingsAction.NotificationsButtonClicked -> {
                _state.update { it.copy(isNotificationsEnabled = !it.isNotificationsEnabled) }
            }

            is SettingsAction.SaveButtonClicked -> {
                updateSettings()
            }

            is SettingsAction.LogoutButtonClicked -> {
                logout()
            }
        }
    }

    private fun logout() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            userRepository.logout()
                .onSuccess(
                    onMessageSuccess = {
                        _state.update { it.copy(isLoading = false) }
                        async { userRepository.recordUserEvent(LogEvent.LOGOUT_EVENT)}.await()
                        _event.send(SettingsEvent.NavigateToSignUp)
                    }
                )
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.getErrorMessage()) }
                }
        }
    }

    private fun updateSettings(){
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val isQuizTimeEnabled = _state.value.isQuizTimeEnabled
            val isCustomTimeEnabled = _state.value.isEnableCustomTimeSwitch
            val customTimeInMinutes = _state.value.customTimeInMinutes
            val selectedLanguage = _state.value.selectedLanguage
            val isNotificationsEnabled = _state.value.isNotificationsEnabled


            if (customTimeInMinutes == 0 && isCustomTimeEnabled){
                _event.send(SettingsEvent.ShowToast(stringProvider.getString(R.string.invalid_custom_time), MessageType.Warning))
                _state.update { it.copy(isLoading = false) }
                return@launch
            }

            Log.i("SettingsViewModel","updateSettings:  selected language: $selectedLanguage ")
            userRepository.updateUserSettings(
                enableNotificationApp = isNotificationsEnabled,
                enableQuizTime = isQuizTimeEnabled,
                switchToCustomTimeInMin = isCustomTimeEnabled,
                customQuizTimeInMin = customTimeInMinutes,
                language = selectedLanguage
            )
                .onSuccess(
                    onMessageSuccess = { message ->
                        viewModelScope.launch {
                            _state.update { it.copy(isLoading = false) }
                            _event.send(SettingsEvent.ShowToast(message, MessageType.Success))
                            delay(500)
                            _event.send(SettingsEvent.RelaunchApp(selectedLanguage))
                        }
                    }
                )
                .onFailure { error ->
                    _event.send(SettingsEvent.ShowToast(error.getErrorMessage(), MessageType.Error))
                    _state.update { it.copy(isLoading = false, error = error.getErrorMessage()) }
                }

        }
    }

    private fun initSettings(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val settings = userRepository.loadSettings()
            _state.update { it.copy(
                isLoading = false,
                isNotificationsEnabled = settings.isEnableNotificationApp,
                selectedLanguage = settings.language,
                isQuizTimeEnabled = settings.isEnableQuizTimeInMin,
                isEnableCustomTimeSwitch = settings.isEnableCustomTimeSwitch,
                customTimeInMinutes = settings.customQuizTimeInMin
            )
            }

        }
    }




}