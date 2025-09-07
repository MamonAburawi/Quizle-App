package com.quizle.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.domain.repository.TopicRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: TopicRepository
): ViewModel() {


    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getQuizTopics()
    }


    fun getQuizTopics() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.loadTopics()
                .onSuccess(
                    onDataSuccess = { topics ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                topics = topics
                            )
                        }
                    }
                )
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            topics = emptyList(),
                            error = error.getErrorMessage()
                        )
                    }
                }

        }
    }



}