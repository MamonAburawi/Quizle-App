package com.quizle.presentation.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizle.domain.repository.QuizQuestionRepository
import com.quizle.domain.repository.QuizTopicRepository
import com.quizle.domain.util.onFailure
import com.quizle.domain.util.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val quizQuestionRepository: QuizQuestionRepository,
    private val quizTopicRepository: QuizTopicRepository
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
            quizTopicRepository.loadTopicById(topicId = topicId)
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
        quizQuestionRepository.loadUserAnswers()
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
        quizQuestionRepository.loadQuizQuestions()
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