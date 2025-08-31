package com.quizle.domain.util



sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class SuccessMessage(val message: String) : Result<String, Nothing>
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>
}

// Updated onSuccess function to handle both success types
inline fun <T, E : Error> Result<T, E>.onSuccess(
    onDataSuccess: (T) -> Unit = {},
    onMessageSuccess: (String) -> Unit = {}
): Result<T, E> {
    when (this) {
        is Result.Success -> onDataSuccess(data)
        is Result.SuccessMessage -> onMessageSuccess(message)
        else -> {}
    }
    return this
}

inline fun <T, E : Error> Result<T, E>.onFailure(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Failure) action(error)
    return this
}



//sealed interface Result<out D, out E: Error> {
//    data class Success<out D>(val data: D): Result<D, Nothing>
//    data class Failure<out E: Error>(val error: E): Result<Nothing, E>
//}
//
//
//inline fun <T,E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
//    if (this is Result.Success) action(data)
//    return this
//}
//
//inline fun <T,E: Error> Result<T, E>.onFailure(action: (E) -> Unit): Result<T, E> {
//    if(this is Result.Failure) action(error)
//    return this
//}