package com.quizle.presentation.util

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
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





enum class RecordUserEvent(val action: String) {
    LOGIN("login"),
    REGISTER("register"),
    LOGOUT("logout"),
    UPDATE_ACCOUNT("update_account"),
    QUIZ_START("quiz_start"),
    QUIZ_END("quiz_end");

//    fun generateLog(userName: String, userId: String): UserActivity {
//        // Implement the logic to create a UserActivity object here
//        return UserActivity(
//            action = this.action,
//            createdAt = System.currentTimeMillis(),
//            userName = userName,
//            userId = userId
//        )
//    }

    companion object {
        fun fromKey(key: String): RecordUserEvent? {
            return entries.find { it.action == key }
        }
    }
}





sealed class Gender(val name: String) {
    data object Male : Gender("male")
    data object Female: Gender("female")

    companion object {
        fun fromKey(key: String): Gender {
           return if (key == "male") Male else Female
        }
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


sealed class IssueType(val key: String) {
    data object IncorrectAnswer : IssueType("incorrect_answer")
    data object QuestionUnclear : IssueType("unclear_question")
    data object TypoGrammarMistake : IssueType("typo_grammar_mistake")
    data class Other(val otherDescription: String) : IssueType(otherDescription) {
    }

    companion object {
        fun fromKey(key: String): IssueType {
            return when (key) {
                "incorrect_answer" -> IncorrectAnswer
                "unclear_question" -> QuestionUnclear
                "typo_grammar_mistake" -> TypoGrammarMistake
                else -> Other(key) // If the key doesn't match, assume it's a custom "Other" type
            }
        }
    }
}






