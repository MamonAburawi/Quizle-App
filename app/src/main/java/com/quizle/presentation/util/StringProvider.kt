package com.quizle.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext
import java.util.Locale


class StringProvider(private val context: Context) {


    fun getString( resId: Int): String {
        return context.getString(resId)
    }

    fun getString( resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }

}