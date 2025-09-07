package com.quizle.domain.module


import kotlinx.serialization.Serializable


@Serializable
data class IssueReport(
    val id: String = "",
    val questionId: String = "",
    val issueType: String = "",
    val additionalComment: String? = null,
    val userId: String = "",
    val createdAt: Long = 0L,
    val isResolved: Boolean? = false
)

