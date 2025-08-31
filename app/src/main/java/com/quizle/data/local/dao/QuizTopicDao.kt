package com.quizle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quizle.data.local.entity.QuizTopicEntity


@Dao
interface QuizTopicDao {

    @Query("SELECT * FROM quiz_topic")
    suspend fun getAllQuizTopic(): List<QuizTopicEntity>

    @Query("SELECT * FROM quiz_topic WHERE id = :topicId")
    suspend fun getQuizTopicByTopicId(topicId: String): QuizTopicEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizTopics(topics: List<QuizTopicEntity>)

    @Query("DELETE FROM quiz_topic")
    suspend fun clearAllQuizTopic()

}