package com.quizle.presentation.screens.quiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.quizle.data.respository.UserRepositoryImpl
import com.quizle.data.utils.LogEvent
import com.quizle.domain.module.QuizResult
import com.quizle.domain.module.UserAnswer
import com.quizle.domain.repository.QuestionRepository
import com.quizle.domain.repository.QuizResultRepository
import com.quizle.domain.repository.TopicRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.navigation.DashboardRoute
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class QuizViewModel(
    private val questionRepository: QuestionRepository,
    private val quizResultRepository: QuizResultRepository,
    private val topicRepository: TopicRepository,
    private val stateHandle: SavedStateHandle,
    private val userRepository: UserRepositoryImpl
): ViewModel(){

    private val topicId = stateHandle.toRoute<DashboardRoute.Quiz>().topicId
//    private val topicCode = 66 // no topic


    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    private val _event = Channel<QuizEvent>()
    val event = _event.receiveAsFlow()


    init {
        Log.i("QuizViewModel", "TopicId: $topicId")
        setUpQuiz()
    }


    fun onAction(action: QuizAction){
        when(action){
            is QuizAction.NextQuestionButtonClick -> {
                val newIndex = (_state.value.currentQuestionIndex + 1)
                    .coerceAtMost(maximumValue = _state.value.questions.lastIndex)
                _state.update { it.copy(currentQuestionIndex = newIndex) }
            }
            is QuizAction.PreviousQuestionButtonClick -> {
                val newIndex = (_state.value.currentQuestionIndex - 1)
                    .coerceAtLeast(minimumValue = 0)
                _state.update { it.copy(currentQuestionIndex = newIndex) }
            }
            is QuizAction.JumpToQuestion -> {
                val currentIndex = action.questionIndex
                _state.update { it.copy(currentQuestionIndex = currentIndex) }
            }

            is QuizAction.SelectOption -> {
                val currentOptionIndex = action.optionIndex
                selectOption(selectedIndex = currentOptionIndex)
            }

            is QuizAction.GetQuizQuestions -> {
                setUpQuiz()
            }

            is QuizAction.ExitQuizButtonClick -> {
                _state.update { it.copy(isQuizExitDialogOpen = true) }
            }
            is QuizAction.ExitQuizDialogConfirm -> {
                viewModelScope.launch {
                    async { userRepository.recordUserEvent(LogEvent.QUIZ_END_EVENT)}.await() // log activity
                    _state.update { it.copy(isQuizExitDialogOpen = false) }
                    _event.trySend(QuizEvent.NavigateToDashboardScreen)
                }
            }
            is QuizAction.ExitQuizDialogDismiss -> {
                _state.update { it.copy(isQuizExitDialogOpen = false) }
            }
            QuizAction.OnTimerFinish -> {
                _state.update { it.copy(isTimeUpDialogOpen = true) }
            }

            QuizAction.TimesUpDialogSeeResultClick -> {
                _state.update { it.copy(topic = it.topic.copy(quizTimeInMin = 0)) }
                saveQuizData()
            }

            QuizAction.CompleteBeforeTimesUp -> {
                saveQuizData()
            }
        }
    }


    private fun saveQuizData(){
        viewModelScope.launch {
            _state.update { it.copy(isSavingInProgress = true) }
            async { saveUserAnswers() }.await()
            async { saveResult() }.await()
            async {userRepository.recordUserEvent(LogEvent.QUIZ_END_EVENT)}.await() // log Activity }
            _state.update { it.copy(isSavingInProgress = false) }
        }
    }

    private suspend fun saveUserAnswers() {
            val answers = _state.value.answers
            questionRepository.saveUserAnswers(answers)
                .onSuccess {}
                .onFailure {}
    }


    private suspend fun applyQuizSettings(){
        userRepository.loadUser()
            .onSuccess(
                onDataSuccess = { user ->
                    val settings = user.settings
                    _state.update { it.copy(
                        quizTimeEnabled = settings.enableQuizTime,
                        switchToCustomTime = settings.switchToCustomTimeInMin,
                        customTimeInMin = settings.customQuizTimeInMin
                    ) }
                }
            )
            .onFailure {}
    }


    private suspend fun saveResult(){
            val answers = _state.value.answers
            val questions = _state.value.questions
            val topic = _state.value.topic
            val quizResult = QuizResult(
                createdAt = System.currentTimeMillis(),
                topicTitleAr = topic.titleArabic,
                topicSubTitleAr = topic.subtitleArabic,
                topicTitleEn = topic.titleEnglish,
                topicSubTitleEn = topic.subtitleEnglish,
                topicTags = topic.tags,
                totalQuestions = questions.size,
                topicId = topic.id,
                correctAnswersCount = answers
                    .count { val question = questions.find { q -> q.id == it.questionId }
                        it.selectedOption == question?.correctAnswer },
            )
            quizResultRepository.saveQuizResult(quizResult)
                .onSuccess {
                    _event.send(QuizEvent.ShowToast(message = "Success! Your quiz results have been saved.", type = MessageType.Success))
                    _event.send(QuizEvent.NavigateToResultScreen)
                }
                .onFailure { error ->
                    _event.send(QuizEvent.ShowToast(message = "An error occurred and your results were not saved.", type = MessageType.Warning))
                }
    }


    fun setUpQuiz(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, questions = emptyList(), loadingMessage = "Setting up quiz...") }
            async { applyQuizSettings() }.await()
            async { getQuizTopic(topicId) }.await()
            async { getQuizQuestions(topicId) }.await()
            async { userRepository.recordUserEvent(LogEvent.QUIZ_START_EVENT)}.await() // log activity
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun getQuizQuestions(topicId: String) {
        val startTimeOut = System.currentTimeMillis()
//        Log.i("QuizViewModel", " start timeout: $startTimeOut")
        questionRepository.loadQuizQuestions(topicId)
            .onSuccess(
                onDataSuccess = { questions ->
                    val endTimeOut = System.currentTimeMillis()
//                    Log.i("QuizViewModel", "end timeout: ${endTimeOut}")
                    Log.i("QuizViewModel", "duration request: ${(endTimeOut - startTimeOut).milliseconds}")
                    _state.update { it.copy(questions = questions) }
                }
            )
            .onFailure { error ->
                _state.update {
                    it.copy(
                        questions = emptyList(),
                        error = error.getErrorMessage()
                    )
                }
            }
    }



    // handle error

    private suspend fun getQuizTopic(topicId: String){
        topicRepository.loadTopicById(topicId)
            .onSuccess (
                onDataSuccess = { topic ->
                    _state.update { it.copy(topic = topic)}
                }
            )
            .onFailure { error ->
                _event.send(QuizEvent.ShowToast(message = error.getErrorMessage(), type = MessageType.Error))
            }
    }

    private fun selectOption(selectedIndex: Int) {
        val currentQuestionIndex = _state.value.currentQuestionIndex
        val quizQuestions = _state.value.questions

        if (quizQuestions.isEmpty() || currentQuestionIndex !in quizQuestions.indices) {
            return
        }

        val currentQuestion = quizQuestions[currentQuestionIndex]
        val questionId = currentQuestion.id

        // 1. Update selectedOptionsMap
        _state.update { currentState ->
            val updatedMap = currentState.selectedOptionsMap
            updatedMap[questionId] = selectedIndex
            currentState.copy(selectedOptionsMap = updatedMap)
        }

        // 2. Prepare to record the user's answer
        val answers = _state.value.answers.toMutableList() // Use 'submittedAnswers' consistently

        // Use the newly selected option to create the UserAnswer
        val userAnswer = UserAnswer(
            questionId = questionId,
            selectedOption = currentQuestion.allOptions[selectedIndex] // Use selectedIndex directly
        )

        // Remove if an answer for this question already exists, then add the new one
        answers.removeIf { it.questionId == questionId }
        answers.add(userAnswer)

        // 3. Update the answers in the state
        _state.update { it.copy(answers = answers) }

    }


    private fun moveToNextQuestion() {
        val quizQuestions = _state.value.questions
        val currentQuestionIndex = _state.value.currentQuestionIndex
        val submittedAnswers = _state.value.answers

        // Get the IDs of questions that have been answered
        val answeredQuestionIds = submittedAnswers.map { it.questionId }.toSet()

        // Find the next question that hasn't been answered yet, starting from the next index
        var nextUnansweredQuestionIndex = -1
        for (i in 1..quizQuestions.size) {
            val nextIndex = (currentQuestionIndex + i) % quizQuestions.size
            if (quizQuestions[nextIndex].id !in answeredQuestionIds) {
                nextUnansweredQuestionIndex = nextIndex
                break
            }
        }
        if (nextUnansweredQuestionIndex != -1) {
            // If an unanswered question is found, move to it
            _state.update { it.copy(currentQuestionIndex = nextUnansweredQuestionIndex) }
        }
    }







}