package com.quizle.data.mapper

import com.quizle.data.local.entity.QuizQuestionEntity
import com.quizle.data.remote.dto.QuizQuestionDto
import com.quizle.domain.module.QuizQuestion


fun QuizQuestionDto.toQuizQuestion(): QuizQuestion {
    return QuizQuestion(
        id = id,
        questionText = questionText,
        correctAnswer = correctAnswer,
        allOptions = (inCorrectAnswers + correctAnswer).shuffled(),
        explanation = explanation,
        topicId = topicId,
        masterOwnerId = masterOwnerId,
        ownersIds = ownersIds,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        reportCount = reportCount,
        level = level,
        tags = tags
    )
}

fun QuizQuestionEntity.entityToQuizQuestion(): QuizQuestion {
    return QuizQuestion(
        id = id,
        questionText = questionText,
        correctAnswer = correctAnswer,
        allOptions = (inCorrectAnswers + correctAnswer).shuffled(),
        explanation = explanation,
        topicId = topicId,
        masterOwnerId = masterOwnerId,
        ownersIds = ownersIds,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        reportCount = reportCount,
        level = level,
        tags = tags
    )
}



fun QuizQuestionDto.toQuizQuestionEntity(): QuizQuestionEntity {
    return QuizQuestionEntity(
        id = id,
        questionText = questionText,
        correctAnswer = correctAnswer,
        inCorrectAnswers = inCorrectAnswers,
        explanation = explanation,
        topicId = topicId,
        masterOwnerId = masterOwnerId,
        ownersIds = ownersIds,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        reportCount = reportCount,
        level = level,
        tags = tags
    )
}


fun QuizQuestion.toQuizQuestionEntity(): QuizQuestionEntity {
    return QuizQuestionEntity(
        id = id,
        questionText = questionText,
        correctAnswer = correctAnswer,
        inCorrectAnswers = allOptions - correctAnswer,
        explanation = explanation,
        topicId = topicId,
        masterOwnerId = masterOwnerId,
        ownersIds = ownersIds,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        reportCount = reportCount,
        level = level,
        tags = tags
    )
}





fun List<QuizQuestionDto>.toQuizQuestions(): List<QuizQuestion> { return map{ it.toQuizQuestion()} }
fun List<QuizQuestionEntity>.entityToQuizQuestions(): List<QuizQuestion> { return map{ it.entityToQuizQuestion()} }
fun List<QuizQuestionDto>.toQuizQuestionEntity(): List<QuizQuestionEntity> { return map{ it.toQuizQuestionEntity()} }
fun List<QuizQuestion>.quizQuestionToEntity(): List<QuizQuestionEntity> { return map{ it.toQuizQuestionEntity()} }