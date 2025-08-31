package com.quizle.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class QuizQuestionDto(
    @SerialName("id") val id: String,
    @SerialName("topic_Id") val topicId: String,
    @SerialName("question_text") val questionText: String,
    @SerialName("correct_answer") val correctAnswer: String,
    @SerialName("master_owner_Id") val masterOwnerId: String,
    @SerialName("incorrect_answers") val inCorrectAnswers: List<String>,
    @SerialName("owners_Ids")  val ownersIds: List<String> = emptyList(),
    @SerialName("img_url") val imageUrl: String?,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    @SerialName("report_count") val reportCount: Int,
    @SerialName("level") val level: String,
    @SerialName("tags") val tags: List<String>,
    @SerialName("explanation") val explanation: String
)



//@Serializable
//data class QuizQuestionDto(
//    val id: String,
//    val correctAnswer: String,
//    val inCorrectAnswers: List<String>,
//    val question: String,
//    val explanation: String,
//    val topicCode: Int
//)
//
