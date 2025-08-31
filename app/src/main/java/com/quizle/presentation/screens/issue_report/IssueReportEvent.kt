package com.quizle.presentation.screens.issue_report

import com.quizle.presentation.common.MessageType


sealed interface IssueReportEvent {
    data object NavigateToQuizScreen: IssueReportEvent
    data class ShowToast(val message: String, val messageType: MessageType) : IssueReportEvent // Event to show a snackbar with a message
}
