package com.quizle.data.respository

import com.quizle.data.mapper.toIssueReportDto
import com.quizle.data.remote.data_source.quiz.RemoteQuizDataSource
import com.quizle.domain.module.IssueReport
import com.quizle.domain.repository.IssueReportRepository
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.Result
import com.quizle.domain.utils.Result.*

class IssueReportRepositoryImpl(
    private val remoteQuizDataSource: RemoteQuizDataSource,
): IssueReportRepository {

    override suspend fun submitIssueReport(issue: IssueReport, token: String): Result<String, ServerDataError> {
        return try {
            when(val result = remoteQuizDataSource.submitIssueReport(issue.toIssueReportDto(), token)){
                is SuccessMessage -> {
                    SuccessMessage(result.message)
                }
                is Failure -> {
                    Failure(result.error)
                }
                is Success -> { // because here in this cause we receive only data class not string
                    Failure(ServerDataError.ConflictServerDataType)
                }
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            Failure(ServerDataError.Unknown)
        }
    }
}