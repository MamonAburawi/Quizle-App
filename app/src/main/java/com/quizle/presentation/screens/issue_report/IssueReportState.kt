package com.quizle.presentation.screens.issue_report

import com.quizle.domain.module.Question
import com.quizle.domain.module.User
import com.quizle.presentation.util.IssueType


data class IssueReportState(
    val question: Question? = null,
    val user: User = User(),
    val issueType: String = IssueType.IncorrectAnswer.key,
    val additionalComment: String? = null,
    val isReportLoading: Boolean = false,
    val submitReportLoading: Boolean = false,
    val loadingMessage: String = "",
    val error: String? = null
)