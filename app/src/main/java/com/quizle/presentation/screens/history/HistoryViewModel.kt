package com.quizle.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.domain.repository.QuizResultRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val quizResultRepository: QuizResultRepository,
): ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    private val _event = Channel<HistoryEvent>()
    val event = _event.receiveAsFlow()


    init {
        loadQuizResults()
    }


    fun onAction(action: HistoryAction){
        when(action){
            is HistoryAction.SearchButtonClicked -> {
                val query = action.query
                loadQuizResults(query)
            }
            is HistoryAction.TryQuizButtonClicked -> {
                val topicId = action.topicId
                viewModelScope.launch {
                    _event.send(HistoryEvent.NavigateToQuiz(topicId))
                }
            }

            is HistoryAction.DeleteButtonClicked -> {
                viewModelScope.launch {
                    async {
                        val quizResultId = action.quizResultId
                        val createdAt = action.createdAt
                        deleteQuizResult(quizResultId = quizResultId, createdAt = createdAt )
                    }.await()
                    async { loadQuizResults() }.await()
                }

            }
        }
    }


    private fun deleteQuizResult(quizResultId: Int, createdAt: Long){
        viewModelScope.launch {
            _state.update { it.copy(deleteLoadingStatus = true) }
            quizResultRepository.deleteQuizResult(quizResultId,createdAt)
                .onSuccess {
                    _state.update { it.copy(deleteLoadingStatus = true) }
                    _event.send(HistoryEvent.ShowMessage("Quiz result deleted successfully", MessageType.Success))
                }
                .onFailure {
                    _state.update { it.copy(deleteLoadingStatus = true) }
                    _event.send(HistoryEvent.ShowMessage(it.getErrorMessage(), MessageType.Error))
                }
        }
    }


    private fun loadQuizResults(query: String? = null){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, isInit = false) }
            delay(300)
            quizResultRepository.searchQuizResults(query)
                .onSuccess(
                    onDataSuccess = { quizResults ->
                        _state.update { it.copy(quizResults = quizResults, isLoading = false, error = null) }
                    }
                )
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.getErrorMessage(), quizResults = emptyList()) }
                }
        }
    }




}