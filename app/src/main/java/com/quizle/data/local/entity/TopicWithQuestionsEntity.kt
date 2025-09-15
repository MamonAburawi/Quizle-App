package com.quizle.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation


// This class holds a Topic and a list of all its Questions.
data class TopicWithQuestionsEntity(
    @Embedded
    val topic: TopicEntity,

    @Relation(
        parentColumn = "id",        // The primary key from TopicEntity
        entityColumn = "topicId"    // The foreign key in QuestionEntity
    )
    val questions: List<QuestionEntity>
)