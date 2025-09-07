package com.quizle.common.utils

import java.lang.Exception

/**
 * Executes the given block and returns its result, or null if an exception occurs.
 */
inline fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


