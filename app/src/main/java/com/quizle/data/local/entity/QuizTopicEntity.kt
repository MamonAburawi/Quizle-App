package com.quizle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quizle.data.util.Constant.QUIZ_TOPIC_TABLE_NAME

@Entity(QUIZ_TOPIC_TABLE_NAME)
data class QuizTopicEntity(
    @PrimaryKey
    val id: String,
    val titleArabic: String,
    val titleEnglish: String,
    val subtitleArabic: String,
    val subtitleEnglish: String,
    val masterOwnerId: String,
    val ownersIds: List<String>,
    val createdAt: Long,
    val updatedAt: Long,
    val topicColor: String,
    val imageUrl: String,
    val tags: List<String>,
    val viewsCount: Int,
    val likeCount: Int,
    val disLikeCount: Int,
    val playedCount: Int,
    val quizTimeInMin: Int?,
    val isDeleted: Boolean,
    val isPublic: Boolean
)
