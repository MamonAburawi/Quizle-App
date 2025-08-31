package com.quizle.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.domain.repository.QuizTopicRepository
import com.quizle.domain.util.onFailure
import com.quizle.domain.util.onSuccess
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: QuizTopicRepository
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