package com.quizle.data.mapper

import com.quizle.data.local.entity.QuizTopicEntity
import com.quizle.data.remote.dto.QuizTopicDto
import com.quizle.data.util.Constant.BASE_URL
import com.quizle.domain.module.QuizTopic



fun QuizTopicDto.toQuizTopic(): QuizTopic{
    return QuizTopic(
        id = id,
        titleArabic = titleArabic,
        titleEnglish = titleEnglish,
        subtitleArabic = subtitleArabic,
        subtitleEnglish = subtitleEnglish,
        topicColor = topicColor,
        imageUrl = imageUrl,
        tags = tags,
        viewsCount = viewsCount,
        likeCount = likeCount,
        disLikeCount = disLikeCount,
        playedCount = playedCount,
        quizTimeInMin = quizTimeInMin,
        isDeleted = isDeleted,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        ownersIds = ownersIds,
        masterOwnerId = masterOwnerId
    )
}

fun QuizTopicEntity.toQuizTopic(): QuizTopic {
    return QuizTopic(
        id = id,
        titleArabic = titleArabic,
        titleEnglish = titleEnglish,
        subtitleArabic = subtitleArabic,
        subtitleEnglish = subtitleEnglish,
        topicColor = topicColor,
        imageUrl = imageUrl,
        tags = tags,
        viewsCount = viewsCount,
        likeCount = likeCount,
        disLikeCount = disLikeCount,
        playedCount = playedCount,
        quizTimeInMin = quizTimeInMin,
        isDeleted = isDeleted,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        ownersIds = ownersIds,
        masterOwnerId = masterOwnerId
    )
}

fun QuizTopicDto.toQuizTopicEntity(): QuizTopicEntity {
    return QuizTopicEntity(
        id = id,
        titleArabic = titleArabic,
        titleEnglish = titleEnglish,
        subtitleArabic = subtitleArabic,
        subtitleEnglish = subtitleEnglish,
        topicColor = topicColor,
        imageUrl =  BASE_URL + imageUrl,
        tags = tags,
        viewsCount = viewsCount,
        likeCount = likeCount,
        disLikeCount = disLikeCount,
        playedCount = playedCount,
        quizTimeInMin = quizTimeInMin,
        isDeleted = isDeleted,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        ownersIds = ownersIds,
        masterOwnerId = masterOwnerId
    )
}



fun List<QuizTopicDto>.entityQuizTopics(): List<QuizTopicEntity> { return  map { it.toQuizTopicEntity() } }
fun List<QuizTopicDto>.toQuizTopics(): List<QuizTopic> { return  map { it.toQuizTopic() } }
fun List<QuizTopicEntity>.entityToQuizTopics(): List<QuizTopic> { return  map { it.toQuizTopic() } }

