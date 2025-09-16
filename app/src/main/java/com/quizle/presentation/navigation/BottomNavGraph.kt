@file:JvmName("DashboardNavGraphKt")

package com.quizle.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.screens.favorite.FavoriteScreen
import com.quizle.presentation.screens.history.HistoryScreen
import com.quizle.presentation.screens.history.HistoryViewModel
import com.quizle.presentation.screens.home.HomeScreen
import com.quizle.presentation.screens.home.HomeViewModel
import com.quizle.presentation.screens.settings.SettingsScreen
import com.quizle.presentation.screens.settings.SettingsViewModel
import com.quizle.presentation.screens.topic.TopicScreen
import com.quizle.presentation.screens.topic.TopicViewModel
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.util.ScaleTransitionDirection
import com.quizle.presentation.util.scaleIntoContainer
import com.quizle.presentation.util.scaleOutOfContainer
import org.koin.androidx.compose.koinViewModel



data class NavBottom(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int
)



@Composable
fun BottomNavGraph(
    bottomNavController: NavHostController,
    startDestination: String = NavBottomRoute.Home.route,
    toastManager: ToastMessageController,
    appNavController: NavHostController,
){
    NavHost(
        modifier = Modifier
            .background(MaterialTheme.extendedColors.backgroundColor),
        navController = bottomNavController,
        startDestination = startDestination,
    ){

        /*** navigation bottom screens ***/
        composable(
            route = NavBottomRoute.Home.route,
            enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
        ) {
            val viewModel = koinViewModel<HomeViewModel>()
            val state = viewModel.state.collectAsState().value
            HomeScreen(
                state = state,
                appNavController = appNavController,
                onAction = viewModel::onAction,
                event = viewModel.event,
                toastManager = toastManager,
            )
        }

        composable(
            route = NavBottomRoute.Settings.route,
            enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
        ){
            val viewModel = koinViewModel<SettingsViewModel>()
            val state = viewModel.state.collectAsState().value
            SettingsScreen(
                state = state,
                toastManager = toastManager,
                appNavController = appNavController,
                event = viewModel.event,
                onAction = viewModel::onAction
            )
        }

        composable(
            route = NavBottomRoute.Topic.route,
            enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
        ){
            val viewModel = koinViewModel<TopicViewModel>()
            val state = viewModel.state.collectAsState().value
            TopicScreen(
                navController = appNavController,
                toastManager = toastManager,
                state = state,
                onAction = viewModel::onAction,
                event = viewModel.event,
            )
        }

        composable(
            route = NavBottomRoute.History.route,
            enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
        ){
            val viewModel = koinViewModel<HistoryViewModel>()
            val state = viewModel.state.collectAsState().value
            HistoryScreen(
                state = state,
                onAction = viewModel::onAction,
                event = viewModel.event,
                toastManager = toastManager,
                navController = bottomNavController,
                appNavController = appNavController
            )
        }

        composable(
            route = NavBottomRoute.Favorite.route,
            enterTransition = { scaleIntoContainer( direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }
        ){
            FavoriteScreen()
        }


    }

}