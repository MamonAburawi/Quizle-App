package com.quizle.presentation.util


import com.quizle.R
import com.quizle.data.utils.LocalDataError
import com.quizle.data.utils.ServerDataError
import com.quizle.domain.utils.ResourceProvider
import org.koin.core.context.GlobalContext



fun ServerDataError.getErrorMessage(): String {
    val resources: ResourceProvider = GlobalContext.get().get()
    return when(this) {
        is ServerDataError.NoInternet -> resources.getString(R.string.error_no_internet)
        is ServerDataError.RequestTimeOut -> resources.getString(R.string.error_request_timeout)
        is ServerDataError.Serialization -> resources.getString(R.string.error_serialization)
        is ServerDataError.Server -> resources.getString(R.string.error_server)
        is ServerDataError.ToManyRequests -> resources.getString(R.string.error_too_many_requests)
        is ServerDataError.RequestEntityTooLarge -> resources.getString(R.string.error_request_entity_too_large)
        is ServerDataError.InternalServerErrorServer -> resources.getString(R.string.error_internal_server)
        is ServerDataError.NotFound -> resources.getString(R.string.error_not_found)
        is ServerDataError.ServiceUnavailable -> resources.getString(R.string.error_service_unavailable)
        is ServerDataError.Unauthorized -> resources.getString(R.string.error_unauthorized)
        is ServerDataError.Unknown -> resources.getString(R.string.error_unknown)
        is ServerDataError.CustomServerErrorServer -> this.message ?: resources.getString(R.string.error_unknown)
        is ServerDataError.ConflictServerDataType -> resources.getString(R.string.error_data_conflict)
    }
}


fun LocalDataError.getErrorMessage(): String {
    val resources: ResourceProvider = GlobalContext.get().get()
    return when (this){
        LocalDataError.NoQuestionFound -> resources.getString(R.string.no_question_found)
        LocalDataError.NoQuizResultFound ->  resources.getString(R.string.no_quiz_result_found)
        LocalDataError.NoTopicFound ->  resources.getString(R.string.no_topic_found)
        LocalDataError.NoUserAnswerFound ->  resources.getString(R.string.no_user_answer_found)
        LocalDataError.NoUserFound ->  resources.getString(R.string.no_user_found)
        LocalDataError.Unknown -> resources.getString(R.string.error_unknown)
    }
}
