package com.quizle.domain.repository

import com.quizle.domain.module.IssueReport
import com.quizle.domain.util.DataError
import com.quizle.domain.util.Result


interface IssueReportRepository {

    suspend fun submitIssueReport(issue: IssueReport, token: String): Result<String,DataError>

}