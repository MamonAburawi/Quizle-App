package com.quizle.presentation.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import java.util.Locale



fun Context.openUrlInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    try {
        startActivity(intent)
    } catch (e: Exception) {
        // Log the exception for debugging
        e.printStackTrace()
    }
}


fun Context.setAppLanguage(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}


fun Context.relaunchApp() {
    val packageManager = this.packageManager
    val intent = packageManager.getLaunchIntentForPackage(this.packageName)
    if (intent != null) {
        val componentName = intent.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        this.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}



fun Int.getChatFromIndex(): String {
    return when(this){
        0 -> "(a)"
        1 -> "(b)"
        2 -> "(c)"
        3 -> "(d)"
        4 -> "(e)"
        5 -> "(f)"
        else -> ""
    }
}







