//package com.quizle.presentation.screens.home

package com.quizle.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.domain.repository.AppReleaseInfoRepository
import com.quizle.domain.repository.QuizResultRepository
import com.quizle.domain.repository.QuizTopicRepository
import com.quizle.domain.repository.UserRepository
import com.quizle.domain.util.onFailure
import com.quizle.domain.util.onSuccess
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.screens.home.HomeEvent.*
import com.quizle.presentation.util.AppVersionHelper
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeViewModel(
    private val topicRepository: QuizTopicRepository,
    private val resultRepository: QuizResultRepository,
    private val userRepository: UserRepository,
    private val appVersionHelper: AppVersionHelper,
    private val appReleaseRepository: AppReleaseInfoRepository

): ViewModel(){



    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _event = Channel<HomeEvent>()
    val event = _event.receiveAsFlow()



    init {
       viewModelScope.launch {
           async { initData() }.await()
           checkUpdates()
       }
    }


    fun refreshData(){
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            async { initData() }.await()
            _state.value = _state.value.copy(isRefreshing = false)
        }
    }


    private suspend fun initData(){
        loadUserStatistics()
        loadUserData()
        loadTopics()
    }


    private suspend fun checkUpdates() {
        _state.update { it.copy(isNewAppUpdatesAvailable = false) }
        val currentAppVersion = appVersionHelper.getVersionCode()
        appReleaseRepository.getAppReleaseInfo()
            .onSuccess(
                onDataSuccess = { newRelease ->
                    Log.i("HomeViewModel","currentAppVersion: $currentAppVersion \n newRelease: ${newRelease}")
                    val newVersionCode = newRelease.versionCode
                    if (currentAppVersion < newVersionCode) {
                        _state.update { it.copy(isNewAppUpdatesAvailable = true, appRelease = newRelease) }
                    }else {
                        _state.update { it.copy(isNewAppUpdatesAvailable = false) }
                    }
                }
            )
            .onFailure {
                _state.update { it.copy(isNewAppUpdatesAvailable = false) }
            }

    }



    private suspend fun loadTopics(){
        _state.value = _state.value.copy(isTopicLoading = true, error = null)
        topicRepository.loadTopics()
            .onSuccess (
                onDataSuccess = { topics ->
                    _state.value = _state.value.copy(isTopicLoading = false, topics = topics)
                }
            )
            .onFailure {
                _state.value = _state.value.copy(isTopicLoading = false, error = it.getErrorMessage() )
                _event.send(HomeEvent.ShowToast(message = it.getErrorMessage(), type = MessageType.Error))
            }
    }

    fun onAction(action: HomeAction){
        when(action){
            is HomeAction.TopicItemCardClicked -> {
                viewModelScope.launch {
                    _event.send(NavigateToQuizScreen(action.topicId))
                }
            }
            is HomeAction.MorePopularQuizzesButtonClicked -> {
                viewModelScope.launch {
                    _event.send(NavigateToTopicScreen(action.key))
                }
            }
            is HomeAction.MoreTopViewedQuizzesButtonClicked -> {
                viewModelScope.launch {
                    _event.send(NavigateToTopicScreen(action.key))
                }
            }
            is HomeAction.ResumeButtonClicked -> {
                viewModelScope.launch {
                    _event.send(ShowToast(message = "Resume Quiz", type = MessageType.Info))
                }
            }

            is HomeAction.EditProfileButtonClicked -> {
                viewModelScope.launch {
                    _event.send(NavigateToProfileScreen)
                }
            }

            HomeAction.OnRefresh -> {
                refreshData()
            }

            HomeAction.NotificationButtonClicked -> {
                viewModelScope.launch {
                    _event.send(NavigateToNotificationScreen)
                }
            }

            HomeAction.UpdateButtonClicked -> {
                viewModelScope.launch {
                    val newVersionUrl = _state.value.appRelease.downloadLink
                    _event.send(NavigateToUrlBrowser(newVersionUrl))
                }
            }
        }
    }

    private  suspend fun loadUserStatistics() {
            resultRepository.getAllQuizResults()
                .onSuccess (
                    onDataSuccess = {
                        quizResults ->
                        val correctAnswersCount = quizResults.sumOf { it.correctAnswersCount }
                        val totalQuestionsCount = quizResults.sumOf { it.totalQuestions }
                        val accurateRate = if (totalQuestionsCount > 0) {
                            (correctAnswersCount.toDouble() / totalQuestionsCount.toDouble()) * 100
                        } else {
                            0.0
                        }
                        _state.update {
                            it.copy(
                                totalQuizzies = quizResults.size,
                                correctAnswers = correctAnswersCount, // This is the single variable with the sum
                                accurateRate = accurateRate.toInt()
                            )
                        }
                    }
                )
                .onFailure {
                    _state.update {
                        it.copy(
                            totalQuizzies = 0,
                            correctAnswers = 0, // This is the single variable with the sum
                            accurateRate = 0
                        )
                    }

                }

    }


    private suspend fun loadUserData() {
        viewModelScope.launch {
            userRepository.loadUser()
                .onSuccess (
                    onDataSuccess = { user ->
                        _state.update { it.copy(user = user) }
                    }
                )
                .onFailure {}
        }
    }



}

