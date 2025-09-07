package com.quizle.common.utils

import java.lang.Exception

inline fun <T> tryOrDefault(defaultValue: T, block: () -> T): T {
    return try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        defaultValue
    }
}