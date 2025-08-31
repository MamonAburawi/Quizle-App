package com.quizle.data.local.prefrences

import android.content.Context
import android.content.SharedPreferences
import com.quizle.domain.module.AppSettings
import kotlinx.serialization.json.Json


class AppPreferences(
    private val context: Context
) {

    companion object{
        private const val APP_PREFERENCES_FILE_NAME = "Quizle_App_Preferences"

        private const val USER_ID = "user_Id"
        private const val APP_LANGUAGE = "app_language"

        private const val ENABLE_NOTIFICATION_APP = "enable_notification_app"

        private const val ENABLE_QUIZ_TIME_IN_MIN = "enable_quiz_time_in_min"

        private const val ENABLE_SWITCH_TO_CUSTOM_TIME_IN_MIN = "enable_switch_to_custom_time_in_min"

        private const val CUSTOM_QUIZ_TIME_IN_MIN = "enable_custom_quiz_time_in_min"

    }

    private val json = Json { prettyPrint = true }
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    private val edit = sharedPreferences.edit()

    // Function to set the user ID
    fun storeUserId(userId: String) {
        edit.putString(USER_ID, userId)
        edit.apply()
    }

    fun storeSettings(
        isEnableNotificationApp: Boolean ,
        isEnableQuizTimeInMin: Boolean ,
        isEnableCustomTimeSwitch: Boolean,
        customQuizTimeInMin: Int,
        language: String,
    ) {
        sharedPreferences.edit().apply {
            putBoolean(ENABLE_NOTIFICATION_APP, isEnableNotificationApp)
            putBoolean(ENABLE_QUIZ_TIME_IN_MIN, isEnableQuizTimeInMin)
            putBoolean(ENABLE_SWITCH_TO_CUSTOM_TIME_IN_MIN,isEnableCustomTimeSwitch)
            putInt(CUSTOM_QUIZ_TIME_IN_MIN, customQuizTimeInMin)
            putString(APP_LANGUAGE, language)
        }.apply()
    }



    fun loadSettings(): AppSettings {
        val enableNotificationApp = sharedPreferences.getBoolean(ENABLE_NOTIFICATION_APP, false)
        val enableQuizTimeInMin = sharedPreferences.getBoolean(ENABLE_QUIZ_TIME_IN_MIN, false)
        val enableCustomTimeSwitch = sharedPreferences.getBoolean(ENABLE_SWITCH_TO_CUSTOM_TIME_IN_MIN, false)
        val customQuizTimeInMin = sharedPreferences.getInt(CUSTOM_QUIZ_TIME_IN_MIN, 0)
        val appLanguage = sharedPreferences.getString(APP_LANGUAGE, "en")

        return AppSettings(
            isEnableNotificationApp = enableNotificationApp,
            isEnableQuizTimeInMin = enableQuizTimeInMin,
            isEnableCustomTimeSwitch = enableCustomTimeSwitch,
            customQuizTimeInMin = customQuizTimeInMin,
            language = appLanguage ?: "en"
        )
    }



//    fun getEnableNotificationApp(): Boolean {
//        // Default value is false if the key doesn't exist
//        return sharedPreferences.getBoolean(ENABLE_NOTIFICATION_APP, false)
//    }
//
//    fun getEnableQuizTimeInMin(): Boolean {
//        // Default value is 0
//        return sharedPreferences.getBoolean(ENABLE_QUIZ_TIME_IN_MIN, false)
//    }
//
//
//    fun getEnableCustomTimeSwitch(): Boolean {
//        // Default value is false
//        return sharedPreferences.getBoolean(ENABLE_SWITCH_TO_CUSTOM_TIME_IN_MIN, false)
//    }
//
//
//    fun getCustomQuizTimeInMin(): Int {
//        return sharedPreferences.getInt(CUSTOM_QUIZ_TIME_IN_MIN, 0)
//    }
//
//
    fun getAppLanguage(): String {
        return sharedPreferences.getString(APP_LANGUAGE, null) ?: "en"
    }

    fun getUserId(): String {
        return sharedPreferences.getString(USER_ID, null) ?: ""
    }

    // Optional: Function to clear the user ID
    fun clearUserId() {
        edit.remove(USER_ID)
        edit.apply()
    }

    fun clearAllPreferences() {
        edit.clear()
        edit.apply()
    }


}