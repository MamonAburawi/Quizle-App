package com.quizle.presentation.screens.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.data.respository.UserRepositoryImpl
import com.quizle.domain.repository.TopicRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.wait


class SplashViewModel(
    private val userRepository: UserRepositoryImpl,
    private val topicRepository: TopicRepository
): ViewModel(){


    private val _state = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    private val _event = Channel<SplashEvent>()
    val event = _event.receiveAsFlow()


    init {
        loadData()
    }

    private fun loadData(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            async { checkIfUserLoggedIn() }.await()
            async { loadTopics() }.await()
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadTopics(){
        topicRepository.loadTopics()
    }

    private suspend fun checkIfUserLoggedIn(){
        userRepository.loadUser()
            .onSuccess(
                onDataSuccess = { user ->
                    val token = user.token
                    val accessToken = token.accessToken
                    if (accessToken.isNotEmpty()){
                        val expiresAt = token.expAt
                        val currentTime = System.currentTimeMillis()
                        val isExpired = currentTime >= expiresAt
                        val isLoggedIn = !isExpired
                        if (isLoggedIn){ // token is valid
                            _event.send(SplashEvent.NavigateToDashboardScreen)
                        }else{ // token is expired
                            userRepository.clearLocalData().wait()
                            _event.send(SplashEvent.NavigateToLoginScreen)
                        }
                    }else { // token is not exist (in case of new user)
                        _event.send(SplashEvent.NavigateToLoginScreen)
                    }
                }
            )
            .onFailure {
                _event.send(SplashEvent.NavigateToLoginScreen)
            }

    }




}