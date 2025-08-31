package com.quizle.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.quizle.data.local.converter.SettingsConverter
import com.quizle.data.local.converter.TokenConverter
import com.quizle.data.local.converter.UserOptionsListConverter
import com.quizle.data.local.dao.QuizQuestionDao
import com.quizle.data.local.dao.QuizResultDao
import com.quizle.data.local.dao.QuizTopicDao
import com.quizle.data.local.dao.UserAnswerDao
import com.quizle.data.local.dao.UserDao
import com.quizle.data.local.entity.QuizQuestionEntity
import com.quizle.data.local.entity.QuizResultEntity
import com.quizle.data.local.entity.QuizTopicEntity
import com.quizle.data.local.entity.UserAnswerEntity
import com.quizle.data.local.entity.UserEntity


@Database(entities = [QuizTopicEntity::class, QuizQuestionEntity::class, UserAnswerEntity::class, UserEntity::class, QuizResultEntity::class] , version = 16, exportSchema = false)
@TypeConverters(UserOptionsListConverter::class, TokenConverter::class, SettingsConverter::class)
abstract class QuizleDatabase : RoomDatabase() {

    abstract fun getQuizTopicDao(): QuizTopicDao
    abstract fun getQuizQuestionDao(): QuizQuestionDao
    abstract fun getUserAnswerDao(): UserAnswerDao
    abstract fun getUserDao(): UserDao
    abstract fun getQuizResultDao(): QuizResultDao

}