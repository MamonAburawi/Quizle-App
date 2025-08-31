package com.quizle.presentation.screens.quiz

sealed interface QuizAction {
    data object PreviousQuestionButtonClick: QuizAction
    data object NextQuestionButtonClick: QuizAction
    data class JumpToQuestion(val questionIndex: Int): QuizAction
    data class SelectOption(val optionIndex: Int): QuizAction
    data object GetQuizQuestions: QuizAction
//    data object SubmitQuestionButtonClick: QuizAction
//    data object SubmitQuizDialogConfirm: QuizAction
//    data object SubmitQuizDialogDismiss: QuizAction
    data object ExitQuizButtonClick : QuizAction
    data object ExitQuizDialogConfirm: QuizAction
    data object ExitQuizDialogDismiss: QuizAction
    data object OnTimerFinish : QuizAction
    data object TimesUpDialogSeeResultClick: QuizAction
    data object CompleteBeforeTimesUp: QuizAction


}