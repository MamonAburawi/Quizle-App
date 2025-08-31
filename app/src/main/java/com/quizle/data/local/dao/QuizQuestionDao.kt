package com.quizle.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quizle.data.local.entity.QuizQuestionEntity

@Dao
interface QuizQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(quizQuestions: List<QuizQuestionEntity>)

    @Query("DELETE FROM quiz_question")
    suspend fun clearAllQuizQuestions()

    @Query("SELECT * FROM quiz_question")
    suspend fun getAllQuizQuestion(): List<QuizQuestionEntity>

    @Query("SELECT * FROM quiz_question WHERE id = :questionId")
    suspend fun getQuizQuestionById(questionId: String): QuizQuestionEntity?

}