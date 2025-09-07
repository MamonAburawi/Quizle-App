package com.quizle.data.utils


/**
    This sealed interface defines all possible errors that can occur during a server data operation.
    It provides a clean and exhaustive way to handle different error states.
 **/

 sealed interface ServerDataError: Error {

    object NoInternet : ServerDataError
    object RequestTimeOut : ServerDataError
    object RequestEntityTooLarge : ServerDataError
    object ToManyRequests : ServerDataError
    object Server : ServerDataError
    object Serialization : ServerDataError
    object Unauthorized : ServerDataError
    object NotFound : ServerDataError
    object ServiceUnavailable : ServerDataError
    object InternalServerErrorServer : ServerDataError
    object Unknown : ServerDataError
    object ConflictServerDataType: ServerDataError
    data class CustomServerErrorServer(val code: Int, val message: String?) : ServerDataError

}

