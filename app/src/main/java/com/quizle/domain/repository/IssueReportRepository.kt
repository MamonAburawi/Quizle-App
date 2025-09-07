package com.quizle.domain.repository

import com.quizle.domain.module.IssueReport
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result


interface IssueReportRepository {

    suspend fun submitIssueReport(issue: IssueReport, token: String): Result<String,ServerDataError>

}