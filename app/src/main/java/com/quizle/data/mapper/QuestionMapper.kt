package com.quizle.data.mapper

import com.quizle.data.local.entity.QuestionEntity
import com.quizle.data.remote.dto.QuestionDto
import com.quizle.domain.module.Question


fun QuestionDto.toQuizQuestion(): Question {
    return Question(
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

fun QuestionEntity.entityToQuizQuestion(): Question {
    return Question(
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



fun QuestionDto.toQuizQuestionEntity(): QuestionEntity {
    return QuestionEntity(
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


fun Question.toQuizQuestionEntity(): QuestionEntity {
    return QuestionEntity(
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





fun List<QuestionDto>.toQuizQuestions(): List<Question> { return map{ it.toQuizQuestion()} }
fun List<QuestionEntity>.entityToQuizQuestions(): List<Question> { return map{ it.entityToQuizQuestion()} }
fun List<QuestionDto>.toQuizQuestionEntity(): List<QuestionEntity> { return map{ it.toQuizQuestionEntity()} }
fun List<Question>.quizQuestionToEntity(): List<QuestionEntity> { return map{ it.toQuizQuestionEntity()} }