package com.quizle.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.dashboard.Dashboard
import com.quizle.presentation.dashboard.DashboardViewModel
import com.quizle.presentation.theme.extendedColors
import org.koin.androidx.compose.koinViewModel


@Composable
fun NestedNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    toastMessageController: ToastMessageController,

) {
    NavHost(
        modifier = modifier
            .background(MaterialTheme.extendedColors.backgroundColor),
        navController = navController,
        startDestination = GraphRoute.Auth
    ) {

        // Auth Graph
        authNavGraph(
            startDestination = AuthRoute.Splash,
            navController = navController,
            toastMessageController = toastMessageController
        )

        // Dashboard Graph
        composable<GraphRoute.Dashboard>() {
            val viewModel = koinViewModel<DashboardViewModel>()
            val state = viewModel.state.collectAsState().value
            Dashboard(
                state = state,
                appNavController = navController,
                toastMessageController = toastMessageController,
            )
        }


        // other screen that attached to root nav graph
        otherScreens(
            navController = navController,
            toastMessageController = toastMessageController
        )


    }
}