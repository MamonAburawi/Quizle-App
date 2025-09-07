package com.quizle.presentation.screens.issue_report


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.quizle.domain.module.IssueReport
import com.quizle.domain.repository.IssueReportRepository
import com.quizle.domain.repository.QuestionRepository
import com.quizle.domain.repository.UserRepository
import com.quizle.domain.utils.onFailure
import com.quizle.domain.utils.onSuccess
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.navigation.DashboardRoute
import com.quizle.presentation.util.IssueType
import com.quizle.presentation.util.getErrorMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class IssueReportViewModel(
    private val questionsRepository: QuestionRepository,
    private val issueReportRepository: IssueReportRepository,
    private val userRepository: UserRepository, // This dependency seems unused in the provided code. Consider removing if not needed.
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(IssueReportState())
    val state = _state.asStateFlow()

    private val _event = Channel<IssueReportEvent>()
    val event = _event.receiveAsFlow()

    private val questionId: String = savedStateHandle.toRoute<DashboardRoute.IssueReport>().questionId

    init {
        initData()
    }

    fun onAction(action: IssueReportAction) {
        when (action) {
            is IssueReportAction.OnAdditionalCommentChanged -> updateAdditionalComment(action.comment)
//            is IssueReportAction.OnEmailChanged -> updateUserEmail(action.email)
            is IssueReportAction.OnIssueTypeSelected -> updateIssueType(action.issueType)
            is IssueReportAction.OnSubmit -> handleSubmitIssueReport()
            is IssueReportAction.ClearError -> clearError()
            is IssueReportAction.BackButtonClicked -> navigateToQuizScreen()
            is IssueReportAction.Refresh -> {
                viewModelScope.launch {
                    loadQuestion(questionId)
                }
            }
            is IssueReportAction.OtherTextChange -> handleOtherTextChange(action.text)
        }
    }


    private fun initData(){
        viewModelScope.launch {
            setLoadingState(isReportLoading = true, submitReportLoading = true, loadingMessage = "Getting question...")
            async { loadQuestion(questionId) }.await()
            async { loadUser() }.await()
//            async { loadToken() }.await()
            setLoadingState(isReportLoading = false, submitReportLoading = false, loadingMessage = "")
        }
    }

    private suspend fun loadUser() {
        userRepository.loadUser()
            .onSuccess (
                onDataSuccess = {user ->
                    _state.update { it.copy(user = user) }
                }
            )
            .onFailure {  }
    }

    private suspend fun loadQuestion(questionId: String) {
        questionsRepository.getQuestionById(questionId)
            .onSuccess (
                onDataSuccess = { question ->
                    _state.update { it.copy(question = question) }
                }
            )
            .onFailure { error ->
                showToast(error.getErrorMessage(), MessageType.Error)
            }
    }

    private fun handleSubmitIssueReport() {
        viewModelScope.launch {
            setLoadingState(submitReportLoading = true)

            val currentQuizQuestion = _state.value.question
            if (currentQuizQuestion == null) {
                showToast("Question data is not loaded. Please refresh.", MessageType.Error)
                setLoadingState(submitReportLoading = false, loadingMessage = "")
                return@launch
            }

            val issueReportToSubmit = createIssueReport(currentQuizQuestion.id)
            val token = _state.value.user.token.accessToken

            issueReportRepository.submitIssueReport(issueReportToSubmit, token)
                .onSuccess {
                    setLoadingState(submitReportLoading = false, loadingMessage = "")
                    showToast("Issue report submitted successfully!", MessageType.Success)
                    navigateToQuizScreen()
                }
                .onFailure { error ->
                    showToast(error.getErrorMessage(), MessageType.Error)
                    setLoadingState(submitReportLoading = false, loadingMessage = "")
                }
        }
    }


    private fun updateAdditionalComment(comment: String) {
        _state.update { it.copy(additionalComment = comment) }
    }



    private fun updateIssueType(issueType: IssueType) {
        _state.update { it.copy(issueType = issueType.key) }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun navigateToQuizScreen() {
        viewModelScope.launch {
            _event.send(IssueReportEvent.NavigateToQuizScreen)
        }
    }

    private fun handleOtherTextChange(text: String) {
        val type = IssueType.fromKey(text)
        if (type is IssueType.Other) {
            _state.update { it.copy(issueType = text) }
        }
    }

    private fun setLoadingState(
        isReportLoading: Boolean = false,
        submitReportLoading: Boolean = false,
        loadingMessage: String = "",
        error: String? = null
    ) {
        _state.update {
            it.copy(
                isReportLoading = isReportLoading,
                submitReportLoading = submitReportLoading,
                loadingMessage = loadingMessage,
                error = error
            )
        }
    }

    private fun showToast(message: String, messageType: MessageType) {
        viewModelScope.launch {
            _event.send(IssueReportEvent.ShowToast(message = message, messageType = messageType))
        }
    }



    private fun createIssueReport(questionId: String): IssueReport {
        return IssueReport(
            questionId = questionId,
            issueType = _state.value.issueType,
            additionalComment = _state.value.additionalComment,
            userId = _state.value.user.id,
        )
    }
}

