package com.quizle.presentation.util

import android.content.Context
import com.quizle.R
import com.quizle.domain.util.DataError
import com.quizle.domain.util.ResourceProvider
import org.koin.core.context.GlobalContext


// Language enum to represent supported languages


//// Result class to get the error message based on language
//class ErrorHandler(private val language: String) {
//
//    fun getErrorMessage(dataError: DataError): String {
//        val errorInfo = when(dataError) {
//            is DataError.NoInternet -> ErrorInfo(
//                messageAr = "لا يوجد اتصال بالإنترنت. يرجى التحقق من الشبكة",
//                messageEn = "No internet connection. Please check your network",
//                status = "NoInternet"
//            )
//            is DataError.RequestTimeOut -> ErrorInfo(
//                messageAr = "انتهت مهلة الطلب. يرجى المحاولة مرة أخرى",
//                messageEn = "Request timed out. Please try again",
//                status = "RequestTimeOut"
//            )
//            is DataError.Serialization -> ErrorInfo(
//                messageAr = "حدث خطأ في معالجة البيانات من الخادم",
//                messageEn = "An error occurred while processing data from the server",
//                status = "Serialization"
//            )
//            is DataError.Server -> ErrorInfo(
//                messageAr = "حدث خطأ من جانب الخادم. يرجى المحاولة مرة أخرى في وقت لاحق",
//                messageEn = "A server error occurred. Please try again later",
//                status = "Server"
//            )
//            is DataError.ToManyRequests -> ErrorInfo(
//                messageAr = "الكثير من الطلبات. يرجى الانتظار والمحاولة مرة أخرى",
//                messageEn = "Too many requests. Please wait and try again",
//                status = "ToManyRequests"
//            )
//            is DataError.RequestEntityTooLarge -> ErrorInfo(
//                messageAr = "الملف كبير جدًا. يرجى اختيار ملف أصغر",
//                messageEn = "The file is too large. Please choose a smaller file",
//                status = "RequestEntityTooLarge"
//            )
//            is DataError.ErrorBody -> ErrorInfo(
//                messageAr = dataError.errorBody?.messageAr ?: "",
//                messageEn = dataError.errorBody?.messageEn ?: "",
//                status = "ErrorBody"
//            )
//            is DataError.Unknown -> ErrorInfo(
//                messageAr = "حدث خطأ غير معروف. يرجى المحاولة مرة أخرى",
//                messageEn = "An unknown error occurred. Please try again",
//                status = "Unknown"
//            )
//            else -> ErrorInfo(
//                messageAr = "حدث خطأ. يرجى المحاولة مرة أخرى",
//                messageEn = "An error occurred. Please try again",
//                status = "Unknown"
//            )
//        }
//
//        // Return the appropriate message based on the language
//        return when (language) {
//            "en" -> errorInfo.messageAr
//            else -> errorInfo.messageEn
//        }
//    }
//}
//






fun DataError.getErrorMessage(): String {
    val resources: ResourceProvider = GlobalContext.get().get()
    return when(this) {
        is DataError.NoInternet -> resources.getString(R.string.error_no_internet)
        is DataError.RequestTimeOut -> resources.getString(R.string.error_request_timeout)
        is DataError.Serialization -> resources.getString(R.string.error_serialization)
        is DataError.Server -> resources.getString(R.string.error_server)
        is DataError.ToManyRequests -> resources.getString(R.string.error_too_many_requests)
        is DataError.RequestEntityTooLarge -> resources.getString(R.string.error_request_entity_too_large)
        is DataError.InternalServerError -> resources.getString(R.string.error_internal_server)
        is DataError.NotFound -> resources.getString(R.string.error_not_found)
        is DataError.ServiceUnavailable -> resources.getString(R.string.error_service_unavailable)
        is DataError.Unauthorized -> resources.getString(R.string.error_unauthorized)
        is DataError.Unknown -> resources.getString(R.string.error_unknown)
        is DataError.CustomServerError -> this.message ?: resources.getString(R.string.error_unknown)
        is DataError.ConflictDataType -> resources.getString(R.string.error_data_conflict)
    }
}



//
//fun DataError.getErrorMessage(): String {
//    return when(this) {
//        is DataError.NoInternet -> "No internet connection. Check your network"
//        is DataError.RequestTimeOut -> "Request timed out. Please try again later."
//        is DataError.Serialization -> "Failed to process data. Try again."
//        is DataError.Server -> "Server error occurred. Please try again later."
//        is DataError.ToManyRequests -> "Too many request. Please slow down."
//        is DataError.RequestEntityTooLarge -> "File upload to large. Please upload a smaller file"
//        is DataError.CustomServerError -> { "" }
//        is DataError.InternalServerError ->    ""
//        is DataError.NotFound ->    ""
//        is DataError.ServiceUnavailable ->    ""
//        is DataError.Unauthorized ->    ""
//        is DataError.Unknown ->   ""
//        else -> ""
//    }
//}