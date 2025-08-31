package com.quizle.data.util

object Constant {

    const val BASE_URL = "http://quizle-env.eba-nqi3xe3b.us-east-1.elasticbeanstalk.com"

    // route
    const val QUESTIONS_ROUTE = "$BASE_URL/quiz/questions"
    const val TOPICS_ROUTE = "$BASE_URL/quiz/topic"
    const val ISSUE_REPORT_ROUTE = "$BASE_URL/issue/report"
    const val USER_ROUTE = "$BASE_URL/user"

    const val USER_UPDATE_ROUTE = "$USER_ROUTE/update"
    const val USER_LOGIN_ROUTE = "$USER_ROUTE/login"
    const val USER_REGISTER_ROUTE = "$USER_ROUTE/register"

    const val USER_LOGOUT_ROUTE = "$USER_ROUTE/force_logout"
    const val TOPICS_SEARCH_ROUTE = "$TOPICS_ROUTE/search"
    const val LOG_USER_ACTIVITY_ROUTE = "$USER_ROUTE/add_activity"

    const val RELEASE_ROUTE = "$BASE_URL/release"

    const val LAST_RELEASE_ROUTE = "$RELEASE_ROUTE/last"

    const val USER_IMAGE_PROFILE_ROUTE = "$USER_ROUTE/imageProfile"
    const val USER_IMAGE_PROFILE_DELETE_ROUTE = "$USER_ROUTE/deleteImageProfile"
    const val USER_IMAGE_PROFILE_ADD_ROUTE = "$USER_ROUTE/addImageProfile"



    // parameter
    const val TOPIC_ID_PARAMETER = "topicId"
    const val USER_ID_PARAMETER = "userId"
    const val USER_EMAIL_PARAMETER = "email"
    const val USER_PASSWORD_PARAMETER = "password"
    const val USER_TOKEN_EXP_PARAMETER = "tokenExp"
    const val TITLE_EN_PARAMETER = "titleEn"
    const val TITLE_AR_PARAMETER = "titleAr"
    const val SUB_TITLE_EN_PARAMETER = "subTitleEn"
    const val SUB_TITLE_AR_PARAMETER = "subTitleAr"
    const val TAG_PARAMETER = "tag"
    const val LIMIT_PARAMETER = "limit"
    const val IMAGE_URL_PARAMETER = "imageUrl"







    // entity
    const val QUIZ_TOPIC_TABLE_NAME = "quiz_topic"
    const val QUIZ_QUESTION_TABLE_NAME = "quiz_question"
    const val USER_ANSWER_TABLE_NAME = "user_answer"
    const val USER_TABLE_NAME = "user"
    const val QUIZ_RESULT_TABLE_NAME = "quiz_result"


    // database
    const val QUIZLE_DATABASE_NAME = "quizle_db"



}