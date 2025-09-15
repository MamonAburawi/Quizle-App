package com.quizle.presentation.screens.result

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.quizle.domain.repository.QuestionRepository
import com.quizle.domain.repository.QuizResultRepository
import com.quizle.domain.repository.TopicRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import com.quizle.presentation.navigation.DashboardRoute
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val topicRepository: TopicRepository,
    private val stateHandle: SavedStateHandle,
    private val quizResultRepository: QuizResultRepository
): ViewModel(){

    private val topicId = stateHandle.toRoute<DashboardRoute.Result>().topicId

    private val _state = MutableStateFlow(ResultState())
    val state = _state.asStateFlow()


    init {
        Log.e("ResultViewModel", "init: -> topicId: $topicId")
        setUpResult()
    }


    suspend fun getQuestionsWithAnswers(){
        quizResultRepository.getQuestionsWithAnswersByTopicId(topicId)
            .onSuccess (
                onDataSuccess = { questionsWithAnswers ->
                    _state.update { it.copy(questionsWithAnswers = questionsWithAnswers) }
                }
            )
            .onFailure { error ->
                _state.update { it.copy(error = error.getErrorMessage()) }
                Log.e("ResultViewModel", "getQuestionsWithAnswers: ${error.getErrorMessage()}")
            }
    }


    private fun setUpResult(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadingMessage = "Initializing result..") }
            async { getQuestionsWithAnswers() }.await()
            async { getQuizTopic() }.await()
            calculateResults()
            _state.update { it.copy(isLoading = false, loadingMessage = "") }
        }
    }

    private suspend fun getQuizTopic() {
            topicRepository.loadTopicById(topicId = topicId)
                .onSuccess (
                    onDataSuccess = { topic ->
                        _state.update { it.copy(topic = topic) }
                    }
                )
                .onFailure { error ->
                    _state.update { it.copy(error = error.getErrorMessage()) }
                }
    }

    private fun calculateResults() {
        val questionsWithAnswers = _state.value.questionsWithAnswers

        // Directly count where the question's correct answer matches the selected option.
        val correctAnswersCount = questionsWithAnswers.count {
            it.question.correctAnswer == it.selectedOption
        }

        _state.update {
            it.copy(
                correctAnswersCount = correctAnswersCount,
                totalQuestionsCount = questionsWithAnswers.size,
            )
        }
    }


//    private fun calculateResults(){
////        val questions = _state.value.questions
////        val answers = _state.value.answers
//        val questions = _state.value.questionsWithAnswers.map {it.question}
//        val answers = _state.value.questionsWithAnswers.map {it.selectedOption}
////        val correctAnswersCount = answers.count { answer ->
////            val question = questions.find { it.id == answer.questionId }
////            question?.correctAnswer == answer.selectedOption
////        }
//
//        val correctAnswersCount = answers.count { answer ->
//            val question = questions.find { it.correctAnswer == answer }
//            question?.correctAnswer == answer
//        }
//
//        _state.update {
//            it.copy(
//                correctAnswersCount = correctAnswersCount,
//                totalQuestionsCount = questions.size,
//            )
//        }
//    }

//    private suspend fun getUserAnswers(){
//        questionRepository.loadUserAnswersByTopicId(topicId)
//            .onSuccess (
//                onDataSuccess = { answers ->
//                    _state.update { it.copy(answers = answers) }
//                }
//            )
//            .onFailure {
//                _state.update { it.copy(error = it.error) }
//            }
//    }


//    private suspend fun getQuizQuestions(){
//        questionRepository.loadQuizQuestions(topicId)
//            .onSuccess(
//                onDataSuccess = { questions ->
//                    _state.update { it.copy(questions = questions) }
//                }
//            )
//            .onFailure {
//                _state.update { it.copy(error = it.error) }
//            }
//    }





}