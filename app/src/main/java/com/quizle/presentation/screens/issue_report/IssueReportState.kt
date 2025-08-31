package com.quizle.presentation.screens.issue_report

import com.quizle.domain.module.QuizQuestion
import com.quizle.domain.module.User
import com.quizle.domain.module.User.Token
import com.quizle.presentation.util.IssueType


data class IssueReportState(
    val quizQuestion: QuizQuestion? = null,
    val user: User = User(),
    val issueType: String = IssueType.IncorrectAnswer.key,
    val additionalComment: String? = null,
    val isReportLoading: Boolean = false,
    val submitReportLoading: Boolean = false,
    val loadingMessage: String = "",
    val error: String? = null
)