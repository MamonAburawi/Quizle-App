package com.quizle.data.mapper

import com.quizle.data.remote.dto.IssueReportDto
import com.quizle.domain.module.IssueReport


fun IssueReport.toIssueReportDto(): IssueReportDto {
    return IssueReportDto(
        id = id,
        questionId = questionId,
        issueType = issueType,
        additionalComment = additionalComment,
        createdAt = createdAt,
        userId = userId,
        isResolved = isResolved
    )
}

fun IssueReportDto.toIssueReport(): IssueReport {
    return IssueReport(
        id = id,
        questionId = questionId,
        issueType = issueType,
        additionalComment = additionalComment,
        createdAt = createdAt,
        userId = userId,
        isResolved = isResolved
    )
}