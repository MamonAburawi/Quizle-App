package com.quizle.data.mapper

import com.quizle.data.local.entity.TopicWithQuestionsEntity
import com.quizle.domain.module.TopicWithQuestions


fun TopicWithQuestionsEntity.toTopicWithQuestions(): TopicWithQuestions {
    return TopicWithQuestions(
        topic = topic.toQuizTopic(),
        questions = questions.entityToQuizQuestions()
    )
}