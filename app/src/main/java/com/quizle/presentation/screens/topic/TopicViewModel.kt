package com.quizle.presentation.screens.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.domain.repository.TopicRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TopicViewModel(
    private val topicRepository: TopicRepository,
): ViewModel(){

    companion object {
        private const val LIMIT = 10
    }

    private val _state = MutableStateFlow(TopicState())
    val state = _state.asStateFlow()

    private val _event = Channel<TopicEvent>()
    val event = _event.receiveAsFlow()




    fun onAction(action: TopicAction){
        when(action){
            is TopicAction.SearchButtonClicked -> {
               viewModelScope.launch {
                   val query = action.query
                   search(query)
               }
            }

            is TopicAction.TopicCardClicked -> {
                viewModelScope.launch {
                    _event.send(TopicEvent.NavigateToQuiz(action.topicId))
                }
            }
        }
    }


    private fun search(query: String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, isInit = false) }
            delay(300)
                topicRepository.searchTopics(query = query, limit = LIMIT)
                    .onSuccess (
                        onDataSuccess = { topics ->
                            _state.update { it.copy(topics = topics, isLoading = false) }
                        }
                    )
                    .onFailure { error ->
                        _state.update { it.copy( error = error.getErrorMessage(), isLoading = false, topics = emptyList()) }
                    }

        }


    }





}