package com.quizle.presentation.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.domain.repository.QuestionRepository
import com.quizle.domain.repository.TopicRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val questionRepository: QuestionRepository,
    private val topicRepository: TopicRepository
): ViewModel(){

    private val _state = MutableStateFlow(ResultState())
    val state = _state.asStateFlow()


    init {
        setUpResult()
    }


    private fun setUpResult(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadingMessage = "Initializing result..") }
            async { getQuizQuestions() }.await()
            async { getUserAnswers() }.await()
            async { getQuizTopic() }.await()
//            getQuizTopic()
            calculateResults()
            _state.update { it.copy(isLoading = false, loadingMessage = "") }
        }
    }

    private suspend fun getQuizTopic() {
            val topicId = _state.value.questions.first().topicId
            topicRepository.loadTopicById(topicId = topicId)
                .onSuccess (
                    onDataSuccess = { topic ->
                        _state.update { it.copy(topic = topic) }
                    }
                )
                .onFailure {}
    }

    private fun calculateResults(){
        val questions = _state.value.questions
        val answers = _state.value.answers

        val correctAnswersCount = answers.count { answer ->
            val question = questions.find { it.id == answer.questionId }
            question?.correctAnswer == answer.selectedOption
        }
        val scorePercentage = correctAnswersCount * 100 / questions.size
        _state.update {
            it.copy(
                correctAnswersCount = correctAnswersCount,
                totalQuestionsCount = questions.size,
                scorePercentage = scorePercentage
            )
        }
    }

    private suspend fun getUserAnswers(){
        questionRepository.loadUserAnswers()
            .onSuccess (
                onDataSuccess = { answers ->
                    _state.update { it.copy(answers = answers) }
                }
            )
            .onFailure {
                _state.update { it.copy(error = it.error) }
            }
    }

    private suspend fun getQuizQuestions(){
        questionRepository.loadQuizQuestions()
            .onSuccess(
                onDataSuccess = {questions ->
                    _state.update { it.copy(questions = questions) }
                }
            )
            .onFailure {
                _state.update { it.copy(error = it.error) }
            }
    }





}