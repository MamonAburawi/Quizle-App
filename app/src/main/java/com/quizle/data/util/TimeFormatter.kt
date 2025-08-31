package com.quizle.data.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object TimeFormatter {


    @JvmOverloads
    fun format(
        date: Date = Date(),
        format: String,
        locale: Locale = Locale.getDefault()
    ): String {
        val simpleDateFormat = SimpleDateFormat(format, locale)
        return simpleDateFormat.format(date)
    }

    /**
     * Example: "20231027_153045"
     */
    fun getCurrentTimeForFileName(): String {
        return format(format = "yyyyMMdd_HHmmss")
    }

    /**
     * Example: "2023-10-27 15:30:45"
     */
    fun getCurrentReadableTime(): String {
        return format(format = "yyyy-MM-dd HH:mm:ss")
    }
}