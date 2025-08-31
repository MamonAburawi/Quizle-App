package com.quizle

import android.app.Application
import com.quizle.di.koinModule
import com.quizle.presentation.util.relaunchApp
import com.quizle.presentation.util.setAppLanguage
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class QuizleApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@QuizleApplication)
            modules(koinModule)
        }





    }
}