package com.quizle.data.utils

import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

/**
  This function provides a general-purpose retry mechanism for any suspendable block of code,
  with configurable attempts, delays, and a custom check to determine if an error is retryable.
 **/

suspend fun <T> retryNetworkRequest(
    fallbackValue: T? = null, // Default to no fallback
    block: suspend () -> T,
): T = retrying(
    fallbackValue = fallbackValue,
    tryCnt = 3, // Total attempts, the request will be retried 2 times
    intervalMillis = { 2000L * it }, // Delay: 2s, 4s
    retryCheck = networkRetryCheck,
    block = block
)


private suspend fun <T> retrying(
    fallbackValue: T?,
    tryCnt: Int,
    intervalMillis: (attempt: Int) -> Long,
    retryCheck: (Throwable) -> Boolean,
    block: suspend () -> T,
): T {
    try {
        val retryCnt = tryCnt - 1
        repeat(retryCnt) { attempt ->
            try {
                return block() // Success, return the result
            } catch (e: Exception) {
                // Don't retry if the coroutine was cancelled or if the check fails
                if (e is CancellationException || !retryCheck(e)) {
                    throw e
                }
            }
            // Wait before the next attempt
            delay(intervalMillis(attempt + 1))
        }
        return block() // Final attempt
    } catch (e: Exception) {
        if (e is CancellationException) {
            throw e
        }
        // If all retries fail, return the fallback value or throw the last error
        return fallbackValue ?: throw e
    }
}


// A check to decide which errors to retry (e.g., not 4xx client errors)
private val networkRetryCheck: (Throwable) -> Boolean = { exception ->
    when {
        // Assuming you have a way to check HTTP error codes from the exception
        // exception.isHttp4xx() -> false
        else -> true // Retry all other errors
    }
}


