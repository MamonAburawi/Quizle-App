package com.quizle.presentation.screens.quiz


import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.quizle.data.local.entity.QuestionWithUserAnswerEntity
import com.quizle.domain.module.Question
import com.quizle.domain.module.QuestionWithUserAnswer
import com.quizle.domain.module.Topic
import com.quizle.domain.module.UserAnswer

data class QuizState(
    val isLoading: Boolean = false,
    val topic: Topic = Topic(),
    val isSavingInProgress: Boolean = false,
    val loadingMessage: String = "",
    val questions: List<Question> = emptyList(),
    val answers: List<UserAnswer> = emptyList(),
    val currentQuestionIndex: Int = 0,
//    val isQuizSubmitDialogOpen: Boolean = false,
    val isQuizExitDialogOpen: Boolean = false,
    val isTimeUpDialogOpen: Boolean = false,
    val selectedOptionsMap: SnapshotStateMap<String, Int> = mutableStateMapOf(), // questionId -> selectedIndex
    val error: String? = null,
    val quizTimeEnabled: Boolean = true,
    val switchToCustomTime: Boolean = false,
    val customTimeInMin: Int = 0


)


