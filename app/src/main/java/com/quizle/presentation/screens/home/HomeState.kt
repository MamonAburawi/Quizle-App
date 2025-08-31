package com.quizle.presentation.screens.home


import com.quizle.domain.module.AppRelease
import com.quizle.domain.module.QuizTopic
import com.quizle.domain.module.User
import com.quizle.presentation.util.Gender


data class HomeState(
    val user: User = User(),
    val totalQuizzies: Int = 0,
    val accurateRate: Int = 0,
    val correctAnswers: Int = 0,
    val isStatisticLoading: Boolean = false,
    val isTopicLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val topics: List<QuizTopic> = emptyList(),
    val isNewAppUpdatesAvailable: Boolean = false,
    val appRelease: AppRelease = AppRelease()
)


//data class HomeState(
//    val user: User = User(),
//    val totalQuizzies: Int = 0,
//    val accurateRate: Int = 0,
//    val correctAnswers: Int = 0,
//    val isStatisticLoading: Boolean = false,
//    val isPopularTopicLoading: Boolean = false,
//    val isTopViewedTopicLoading: Boolean = false,
//    val isRefreshing: Boolean = false,
//    val popularLoadError: String? = null,
//    val topViewedLoadError: String? = null,
//    val hasQuizPending: Boolean = true,
//    val popularTopics: List<QuizTopic> = emptyList(),
//    val topViewedTopics: List<QuizTopic> = emptyList()
//)