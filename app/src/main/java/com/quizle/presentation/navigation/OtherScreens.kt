package com.quizle.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.screens.issue_report.IssueReportScreen
import com.quizle.presentation.screens.issue_report.IssueReportViewModel
import com.quizle.presentation.screens.notifications.NotificationScreen
import com.quizle.presentation.screens.profile.ProfileScreen
import com.quizle.presentation.screens.profile.ProfileViewModel
import com.quizle.presentation.screens.quiz.QuizScreen
import com.quizle.presentation.screens.quiz.QuizViewModel
import com.quizle.presentation.screens.result.ResultScreen
import com.quizle.presentation.screens.result.ResultViewModel
import com.quizle.presentation.util.ScaleTransitionDirection
import com.quizle.presentation.util.scaleIntoContainer
import com.quizle.presentation.util.scaleOutOfContainer
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.otherScreens(
    navController: NavHostController,
    toastMessageController: ToastMessageController
){
    composable<DashboardRoute.Notifications>(
        enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
        exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
    ){
        NotificationScreen()
    }

    composable<DashboardRoute.Profile>(
        enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
        exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
    ){
        val viewModel = koinViewModel<ProfileViewModel>()
        val state = viewModel.state.collectAsState().value
        ProfileScreen(
            state = state,
            event = viewModel.event,
            onAction = viewModel::onAction,
            toastManager = toastMessageController
        )
    }


    composable<DashboardRoute.Quiz>(
        enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
        exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
    ){
        val viewModel = koinViewModel<QuizViewModel>()
        val state = viewModel.state.collectAsState().value
        QuizScreen(
            state = state,
            navController = navController,
            onAction = viewModel::onAction,
            event = viewModel.event,
            onRefresh = viewModel::setUpQuiz,
            toastManager = toastMessageController
        )
    }

    composable<DashboardRoute.IssueReport>(
        enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
        exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
    ) {
        val viewModel = koinViewModel<IssueReportViewModel>()
        val state = viewModel.state.collectAsState().value
        IssueReportScreen(
            navController = navController,
            toastMessageController = toastMessageController,
            state = state,
            event = viewModel.event,
            onAction = viewModel::onAction
        )
    }
    composable<DashboardRoute.Result>(
        enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
        exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
    ){
        val viewModel = koinViewModel<ResultViewModel>()
        val state = viewModel.state.collectAsState().value
        ResultScreen(
            navController = navController,
            state = state
        )
    }





}
