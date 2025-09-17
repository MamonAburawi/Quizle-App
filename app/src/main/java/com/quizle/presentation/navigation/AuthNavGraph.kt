package com.quizle.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.screens.login.LoginScreen
import com.quizle.presentation.screens.login.LoginViewModel
import com.quizle.presentation.screens.sign_up.SignUpScreen

import com.quizle.presentation.screens.sign_up.SignUpViewModel


import com.quizle.presentation.screens.splash_screen.SplashScreen
import com.quizle.presentation.screens.splash_screen.SplashViewModel
import com.quizle.presentation.util.ScaleTransitionDirection
import com.quizle.presentation.util.scaleIntoContainer
import com.quizle.presentation.util.scaleOutOfContainer
import org.koin.androidx.compose.koinViewModel


fun NavGraphBuilder.authNavGraph(
    startDestination: AuthRoute = AuthRoute.Splash,
    toastMessageController: ToastMessageController,
    navController: NavHostController
) {

    navigation<GraphRoute.Auth>( startDestination = startDestination){

        composable<AuthRoute.Splash>(
            enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
        ){
            val viewModel = koinViewModel<SplashViewModel>()
            val state = viewModel.state.collectAsState().value
            SplashScreen(
                navController = navController,
                state = state,
                toastMessageController = toastMessageController,
                event = viewModel.event
            )
        }


        composable<AuthRoute.Login>(
            enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
        ){
            val viewModel = koinViewModel<LoginViewModel>()
            val state = viewModel.state.collectAsState().value
            LoginScreen(
                navController = navController,
                state = state,
                toastMessageController = toastMessageController,
                onAction = viewModel::onAction,
                event = viewModel.event
            )
        }

        composable<AuthRoute.SignUp>(
            enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
        ){
            val viewModel = koinViewModel<SignUpViewModel>()
            val state = viewModel.state.collectAsState().value
            SignUpScreen(
                navController = navController,
                state = state,
                toastMessageController = toastMessageController,
                onAction = viewModel::onAction,
                event = viewModel.event
            )
//            SignUpScreen(
//                navController = navController,
//                state = state,
//                toastMessageController = toastMessageController,
//                onAction = viewModel::onAction,
//                event = viewModel.event
//            )
        }

    }

}