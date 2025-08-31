package com.quizle.data.local.database

import android.content.Context
import androidx.room.Room
import com.quizle.data.util.Constant.QUIZLE_DATABASE_NAME

object QuizleDatabaseFactory {

    fun create(context: Context): QuizleDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = QuizleDatabase::class.java,
            name = QUIZLE_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
//            .addMigrations()
            .build()
    }
}