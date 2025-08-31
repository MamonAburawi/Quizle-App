package com.quizle.presentation.screens.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.R
import com.quizle.domain.repository.QuizTopicRepository
import com.quizle.domain.util.onFailure
import com.quizle.domain.util.onSuccess
import com.quizle.presentation.util.StringProvider
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TopicViewModel(
    private val topicRepository: QuizTopicRepository,
    private val stringProvider: StringProvider
): ViewModel(){

    companion object {
        private const val LIMIT = 10
    }

    private val _state = MutableStateFlow(TopicState())
    val state = _state.asStateFlow()

    private val _event = Channel<TopicEvent>()
    val event = _event.receiveAsFlow()


    init {
        _state.update { it.copy(initMessage = stringProvider.getString(R.string.try_to_search_for_a_topic)) }
    }

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
            _state.update { it.copy(isLoading = true, error = null, initMessage = "") }
            delay(300)
                topicRepository.searchTopics(query = query, limit = LIMIT)
                    .onSuccess (
                        onDataSuccess = { topics ->
                            _state.update { it.copy(topics = topics, isLoading = false, initMessage = "") }
                        }
                    )
                    .onFailure { error ->
                        _state.update { it.copy(initMessage = "", error = error.getErrorMessage(), isLoading = false, topics = emptyList()) }
                    }

        }


    }





}