package com.quizle.data.mapper

import com.quizle.data.local.entity.TopicEntity
import com.quizle.data.remote.dto.TopicDto
import com.quizle.data.utils.Constant.BASE_URL
import com.quizle.domain.module.Topic



fun TopicDto.toQuizTopic(): Topic{
    return Topic(
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

fun TopicEntity.toQuizTopic(): Topic {
    return Topic(
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

fun TopicDto.toQuizTopicEntity(): TopicEntity {
    return TopicEntity(
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



fun List<TopicDto>.entityQuizTopics(): List<TopicEntity> { return  map { it.toQuizTopicEntity() } }
fun List<TopicDto>.toQuizTopics(): List<Topic> { return  map { it.toQuizTopic() } }
fun List<TopicEntity>.entityToQuizTopics(): List<Topic> { return  map { it.toQuizTopic() } }

