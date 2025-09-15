package com.quizle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.quizle.data.local.entity.QuestionWithUserAnswerEntity
import com.quizle.data.local.entity.QuizResultEntity

@Dao
interface QuizResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(quizResult: QuizResultEntity)

    @Query("SELECT * FROM quiz_result WHERE id == :resultId")
    suspend fun getQuizResultById(resultId: String): QuizResultEntity?

    @Query("SELECT * FROM quiz_result")
    suspend fun getAllQuizResults(): List<QuizResultEntity>

    @Query("DELETE FROM quiz_result WHERE id == :quizResultId AND createdAt == :createdAt")
    suspend fun deleteQuizResult(quizResultId: Int, createdAt: Long)

    @Query("DELETE FROM quiz_result")
    suspend fun deleteAllQuizResults()
//
//    @Query("SELECT * FROM quiz_result WHERE topicTitleEn LIKE '%' || :query || '%' OR topicTitleAr LIKE '%' || :query || '%' OR topicSubTitleEn LIKE '%' || :query || '%' OR topicSubTitleAr LIKE '%' || :query || '%' OR topicTags LIKE '%' || :query || '%'")
//    suspend fun searchQuizResults(query: String?): List<QuizResultEntity>

    @Query(
        " SELECT * FROM quiz_result\n" +
                "WHERE (:query IS NULL OR :query = '') OR\n" +
                "(topicTitleEn LIKE '%' || :query || '%'\n" +
                "OR topicTitleAr LIKE '%' || :query || '%'\n" +
                "OR topicSubTitleEn LIKE '%' || :query || '%'\n" +
                "OR topicSubTitleAr LIKE '%' || :query || '%'\n" +
                "OR topicTags LIKE '%' || :query || '%')\n" +
                " ORDER BY createdAt DESC"

    )
    suspend fun searchQuizResults(query: String?): List<QuizResultEntity>



    @Transaction
    @Query("""
        SELECT 
             questions.*, userAnswer.selectedOption 
        FROM 
            quiz_question AS questions
        LEFT JOIN 
            user_answer AS userAnswer ON questions.id = userAnswer.questionId
        WHERE 
            questions.topicId = :topicId
    """)
    suspend fun getQuestionsWithAnswersByTopicId(topicId: String): List<QuestionWithUserAnswerEntity>





}