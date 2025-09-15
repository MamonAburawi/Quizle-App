package com.quizle.presentation.util

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.quizle.data.local.prefrences.AppPreferences
import org.koin.java.KoinJavaComponent.inject
import java.util.Locale


object LocaleHelper{
    private val appPreferences: AppPreferences by inject(
        clazz = AppPreferences::class.java,
    )

    fun setLocale(context: Context, languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
        appPreferences.setAppLanguage(languageCode)
    }

    // This function shares us the context needed before firing up our activity
    fun updateContext(context: Context): Context {
        val language = appPreferences.getAppLanguage()
        val locale = Locale.Builder()
            .setLanguage(language)
            .build()

        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    fun getLanguage() = appPreferences.getAppLanguage()

}