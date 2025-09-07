package com.quizle.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.quizle.data.local.converter.SettingsConverter
import com.quizle.data.local.converter.TokenConverter
import com.quizle.data.local.converter.UserOptionsListConverter
import com.quizle.data.local.dao.QuestionDao
import com.quizle.data.local.dao.QuizResultDao
import com.quizle.data.local.dao.TopicDao
import com.quizle.data.local.dao.UserAnswerDao
import com.quizle.data.local.dao.UserDao
import com.quizle.data.local.entity.QuestionEntity
import com.quizle.data.local.entity.QuizResultEntity
import com.quizle.data.local.entity.TopicEntity
import com.quizle.data.local.entity.UserAnswerEntity
import com.quizle.data.local.entity.UserEntity


@Database(entities = [TopicEntity::class, QuestionEntity::class, UserAnswerEntity::class, UserEntity::class, QuizResultEntity::class] , version = 16, exportSchema = false)
@TypeConverters(UserOptionsListConverter::class, TokenConverter::class, SettingsConverter::class)
abstract class QuizleDatabase : RoomDatabase() {

    abstract fun getQuizTopicDao(): TopicDao
    abstract fun getQuizQuestionDao(): QuestionDao
    abstract fun getUserAnswerDao(): UserAnswerDao
    abstract fun getUserDao(): UserDao
    abstract fun getQuizResultDao(): QuizResultDao

}