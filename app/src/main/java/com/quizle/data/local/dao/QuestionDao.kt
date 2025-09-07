package com.quizle.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quizle.data.local.entity.QuestionEntity

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(quizQuestions: List<QuestionEntity>)

    @Query("DELETE FROM quiz_question")
    suspend fun clearAllQuizQuestions()

    @Query("SELECT * FROM quiz_question")
    suspend fun getAllQuizQuestion(): List<QuestionEntity>

    @Query("SELECT * FROM quiz_question WHERE topicId = :topicId")
    suspend fun getQuizQuestionsByTopicId(topicId: String): List<QuestionEntity>

    @Query("SELECT * FROM quiz_question WHERE id = :questionId")
    suspend fun getQuizQuestionById(questionId: String): QuestionEntity?

}