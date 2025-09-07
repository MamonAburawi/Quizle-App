package com.quizle.data.utils


/** you can use this interface if you want to handle the local error data **/
sealed interface LocalDataError: Error {

    object NoTopicFound: LocalDataError
    object NoQuestionFound: LocalDataError
    object NoUserFound: LocalDataError
    object NoQuizResultFound: LocalDataError
    object NoUserAnswerFound: LocalDataError

    object Unknown: LocalDataError

}