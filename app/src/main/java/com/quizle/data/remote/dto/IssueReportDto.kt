package com.quizle.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class IssueReportDto(
    @SerialName("id") val id: String,
    @SerialName("question_id") val questionId: String,
    @SerialName("issue_type") val issueType: String,
    @SerialName("additional_comment") val additionalComment: String?,
    @SerialName("user_id") val userId: String,
    @SerialName("create_at") val createdAt: Long,
    @SerialName("is_resolved") val isResolved: Boolean?
)


//@Serializable
//data class IssueReportDto(
//    val id: String,
//    val questionId: String,
//    val issueType: String,
//    val additionalComment: String?,
//    val userEmail: String?,
//    val timeStamp: String
//)
