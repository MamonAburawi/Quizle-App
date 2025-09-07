@file:Suppress("UNCHECKED_CAST")

package com.quizle.data.utils

import com.quizle.domain.module.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import com.quizle.domain.utils.Result
import io.ktor.http.contentLength
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException

/**
  This code provides a centralized and safe way to execute network requests. It handles a wide range of potential network and server errors,
  such as a lost internet connection or a request timeout.
 **/

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, ServerDataError> {
    val response = try {
        execute()
    } catch (ex: UnknownHostException) {
        ex.printStackTrace()
        return Result.Failure(ServerDataError.NoInternet)
    } catch (ex: UnresolvedAddressException) {
        ex.printStackTrace()
        return Result.Failure(ServerDataError.NoInternet)
    } catch (ex: SocketTimeoutException) {
        ex.printStackTrace()
        return Result.Failure(ServerDataError.RequestTimeOut)
    } catch (ex: Exception) {
        ex.printStackTrace()
        return Result.Failure(ServerDataError.Unknown)
    }

    if (response.status.isSuccess()) {
        return when (T::class) {
            // Case 1: The function is expected to return a String message
            String::class -> {
                try {
                    val message = response.body<String>()
                    Result.SuccessMessage(message) as Result<T, ServerDataError>
                } catch (e: SerializationException) {
                    e.printStackTrace()
                    Result.Failure(ServerDataError.Serialization)
                }
            }
            // Case 2: The function is expected to return a data class
            else -> {
                // If there's no content, it's an unexpected success for a data class
                if (response.contentLength() == 0L) {
                    return Result.Failure(ServerDataError.Unknown)
                }
                try {
                    val data = response.body<T>()
                    Result.Success(data)
                } catch (ex: SerializationException) {
                    ex.printStackTrace()
                    Result.Failure(ServerDataError.Serialization)
                }
            }
        }
    }

    return try {
        val errorBody = response.body<ErrorResponse>()
        Result.Failure(ServerDataError.CustomServerErrorServer(response.status.value, errorBody.message))
    } catch (e: Exception) {
        return Result.Failure(ServerDataError.Unknown)
    }
}
