package com.quizle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quizle.data.local.entity.UserAnswerEntity

@Dao
interface UserAnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAnswers(userAnswers: List<UserAnswerEntity>)

    @Query("DELETE FROM user_answer")
    suspend fun clearAllUserAnswers()

    @Query("SELECT * FROM user_answer WHERE questionId = :questionId")
    suspend fun getUserAnswerById(questionId: String): UserAnswerEntity?

    @Query("SELECT * FROM user_answer")
    suspend fun getAllUserAnswers(): List<UserAnswerEntity>
}