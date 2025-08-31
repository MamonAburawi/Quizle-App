package com.quizle.data.remote



import com.quizle.domain.module.ErrorResponse
import com.quizle.domain.util.DataError
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import com.quizle.domain.util.Result
import io.ktor.http.contentLength
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException


@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, DataError> {
    val response = try {
        execute()
    } catch (ex: UnknownHostException) {
        ex.printStackTrace()
        return Result.Failure(DataError.NoInternet)
    } catch (ex: UnresolvedAddressException) {
        ex.printStackTrace()
        return Result.Failure(DataError.NoInternet)
    } catch (ex: SocketTimeoutException) {
        ex.printStackTrace()
        return Result.Failure(DataError.RequestTimeOut)
    } catch (ex: Exception) {
        ex.printStackTrace()
        return Result.Failure(DataError.Unknown)
    }

    if (response.status.isSuccess()) {
        return when (T::class) {
            // Case 1: The function is expected to return a String message
            String::class -> {
                try {
                    val message = response.body<String>()
                    Result.SuccessMessage(message) as Result<T, DataError>
                } catch (e: SerializationException) {
                    Result.Failure(DataError.Serialization)
                }
            }
            // Case 2: The function is expected to return a data class
            else -> {
                // If there's no content, it's an unexpected success for a data class
                if (response.contentLength() == 0L) {
                    return Result.Failure(DataError.Unknown)
                }
                try {
                    val data = response.body<T>()
                    Result.Success(data)
                } catch (ex: SerializationException) {
                    ex.printStackTrace()
                    Result.Failure(DataError.Serialization)
                }
            }
        }
    }

    return try {
        val errorBody = response.body<ErrorResponse>()
        Result.Failure(DataError.CustomServerError(response.status.value, errorBody.message))
    } catch (e: Exception) {
        return Result.Failure(DataError.Unknown)
    }
}

//suspend inline fun <reified T> safeCall(
//    execute: () -> HttpResponse
//): Result<T, DataError> {
//    val response = try {
//        execute()
//    } catch (e: UnknownHostException) {
//        e.printStackTrace()
//        return Result.Failure(DataError.NoInternet)
//    } catch (e: UnresolvedAddressException) {
//        e.printStackTrace()
//        return Result.Failure(DataError.NoInternet)
//    } catch (e: SocketTimeoutException) {
//        e.printStackTrace()
//        return Result.Failure(DataError.RequestTimeOut)
//    } catch (e: Exception) {
//        e.printStackTrace()
//        return Result.Failure(DataError.Unknown)
//    }
//
//    // error from the server
//    val errorBody = response.body<ErrorResponse>()
//    return Result.Failure(DataError.CustomServerError(response.status.value, errorBody.message))
//}



//    return when (response.status.value) {
//        in 200..299 -> {
//            try {
//                Result.Success(response.body<T>())
//            } catch (e: Exception) { // Catch all exceptions related to body deserialization
//                e.printStackTrace()
//                Result.Failure(DataError.Serialization)
//            }
//        }
//        // Client-side mapping of server errors
//        401 -> {
//            val errorBody = try { response.body<ErrorResponse>() } catch (e: Exception) { null }
//            Result.Failure(DataError.Unauthorized)
//        }
//        404 -> {
//            val errorBody = try { response.body<ErrorResponse>() } catch (e: Exception) { null }
//            Result.Failure(DataError.NotFound)
//        }
//        413 -> Result.Failure(DataError.RequestEntityTooLarge)
//        408 -> Result.Failure(DataError.RequestTimeOut)
//        429 -> Result.Failure(DataError.ToManyRequests)
//        // Server-side errors
//        500 -> {
//            val errorBody = try { response.body<ErrorResponse>() } catch (e: Exception) { null }
//            Result.Failure(DataError.InternalServerError)
//        }
//        503 -> {
//            val errorBody = try { response.body<ErrorResponse>() } catch (e: Exception) { null }
//            Result.Failure(DataError.ServiceUnavailable)
//        }
//        in 500..599 -> Result.Failure(DataError.Server)
//        // Handle all other server-sent error messages
//        else -> {
//            val errorBody = try { response.body<ErrorResponse>() } catch (e: Exception) { null }
//            Result.Failure(DataError.CustomServerError(response.status.value, errorBody?.message))
//        }
//    }
//}



//suspend inline fun <reified T> safeCall(
//    execute: () -> HttpResponse
//): Result<T, DataError> {
//    val response = try {
//        execute()
//    } catch (e: UnknownHostException) {
//        return Result.Failure(DataError.NoInternet)
//    } catch (e: UnresolvedAddressException) {
//        return Result.Failure(DataError.NoInternet)
//    } catch (e: SocketTimeoutException) {
//        return Result.Failure(DataError.RequestTimeOut)
//    } catch (e: Exception) {
//        e.printStackTrace()
//        return Result.Failure(DataError.Unknown(e.message))
//    }
//    val errorBody = try { response.body<ErrorInfo>() } catch (e: Exception) { null }
//
//    return when (response.status.value) {
//        in 200..299 -> {
//            try {
//                Result.Success(response.body<T>())
//            } catch (e: JsonConvertException) {
//                e.printStackTrace()
//                Result.Failure(DataError.Serialization)
//            }  catch (e: NoTransformationFoundException) {
//                e.printStackTrace()
//                Result.Failure(DataError.Serialization)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                return Result.Failure(DataError.Unknown(e.message))
//            }
//        }
//        413 -> Result.Failure(DataError.RequestEntityTooLarge)
//        408 -> Result.Failure(DataError.RequestTimeOut)
//        429 -> Result.Failure(DataError.ToManyRequests)
//        in 500..599 -> Result.Failure(DataError.Server)
//        else -> {
//            Result.Failure(DataError.ErrorBody(errorBody = errorBody))
//        }
//    }
//}