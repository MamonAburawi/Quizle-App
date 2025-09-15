package com.quizle.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.quizle.data.utils.Constant.QUIZ_QUESTION_TABLE_NAME

@Entity(
    tableName = QUIZ_QUESTION_TABLE_NAME,
//    foreignKeys = [
//        ForeignKey(
//            entity = TopicEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["topicId"],
//            onDelete = ForeignKey.CASCADE // If a topic is deleted, its questions are also deleted
//        )
//    ]
)
data class QuestionEntity(
    @PrimaryKey
    val id: String,
    val topicId: String,
    val ownersIds: List<String>,
    val masterOwnerId: String,
    val questionText: String,
    val correctAnswer: String,
    val inCorrectAnswers: List<String>,
    val imageUrl: String?,

    val createdAt: Long,
    val updatedAt: Long,
    val reportCount: Int,
    val level: String,
    val tags: List<String>,
    val explanation: String
)


