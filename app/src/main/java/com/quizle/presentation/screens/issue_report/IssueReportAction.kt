package com.quizle.presentation.screens.issue_report

import com.quizle.data.utils.IssueType



sealed interface IssueReportAction {
    data object BackButtonClicked: IssueReportAction
    data class OnIssueTypeSelected(val issueType: IssueType) : IssueReportAction
    data object OnSubmit : IssueReportAction
    data object ClearError: IssueReportAction
    data object Refresh : IssueReportAction
    data class OnAdditionalCommentChanged(val comment: String) : IssueReportAction
//    data class OnEmailChanged(val email: String): IssueReportAction
    data class OtherTextChange(val text: String) : IssueReportAction
}