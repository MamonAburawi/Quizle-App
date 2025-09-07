package com.quizle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quizle.data.local.entity.TopicEntity


@Dao
interface TopicDao {

    @Query("SELECT * FROM quiz_topic")
    suspend fun getAllQuizTopic(): List<TopicEntity>

    @Query("SELECT * FROM quiz_topic WHERE id = :topicId")
    suspend fun getQuizTopicByTopicId(topicId: String): TopicEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizTopics(topics: List<TopicEntity>)

    @Query("DELETE FROM quiz_topic")
    suspend fun clearAllQuizTopic()

}