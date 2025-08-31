package com.quizle.domain.util





 sealed interface DataError: Error {
    object NoInternet : DataError
    object RequestTimeOut : DataError
    object RequestEntityTooLarge : DataError
    object ToManyRequests : DataError
    object Server : DataError
    object Serialization : DataError
    object Unauthorized : DataError
    object NotFound : DataError
    object ServiceUnavailable : DataError
    object InternalServerError : DataError
    object Unknown : DataError

    object ConflictDataType: DataError



    data class CustomServerError(val code: Int, val message: String?) : DataError



     // specific error message type

}


//interface DataError: Error {
//    data object RequestTimeOut: DataError
//    data object ToManyRequests: DataError
//    data object Server: DataError
//    data object NoInternet: DataError
//    data object Serialization: DataError
//    data object RequestEntityTooLarge: DataError
//    data class ErrorBody(val errorBody: ErrorInfo?): DataError
//    data class Unknown(val errorMessage: String? = null): DataError
//}

