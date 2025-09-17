@file:OptIn(ExperimentalMaterial3Api::class)

package com.quizle.presentation.dashboard

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.quizle.R
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.common.rememberToastMessageController
import com.quizle.presentation.navigation.BottomNavGraph
import com.quizle.presentation.navigation.NavBottom
import com.quizle.presentation.navigation.NavBottomRoute
import com.quizle.presentation.theme.extendedColors

@Composable
fun Dashboard(
    state: DashboardState,
    toastMessageController: ToastMessageController,
    appNavController: NavHostController
) {
    val navController = rememberNavController()
    val navBottomItems = createNavBottomItems()
    var currentNavBottom by remember { mutableStateOf(navBottomItems.first()) }

    val showTopBar = currentNavBottom.route == NavBottomRoute.Home.route
    SetupBottomNavigationListener(navController, navBottomItems) { newNavBottom ->
        currentNavBottom = newNavBottom
    }
    HandleBackPress(currentNavBottom)

        Scaffold(
//            topBar = {
//                if (showTopBar) {
//                    DashboardAppBar(
//                        title = currentNavBottom.label,
//                        onNotificationClick = { appNavController.navigateToNotifications() }
//                    )
//                }
//            },
            bottomBar = {
                DashboardBottomBar(
                    items = navBottomItems,
                    selectedRoute = currentNavBottom.route,
                    onItemSelected = { selectedItem ->
                        if (currentNavBottom.route != selectedItem.route) {
                            currentNavBottom = selectedItem
                            navController.navigate(selectedItem.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            DashBoardContent(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                appNavController = appNavController,
                toastMessageController = toastMessageController
            )
        }



}

@Composable
fun DashBoardContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appNavController: NavHostController,
    toastMessageController: ToastMessageController
) {
    Column(modifier = modifier) {
        BottomNavGraph(
            bottomNavController = navController,
            appNavController = appNavController,
            toastManager = toastMessageController
        )
    }
}
/**
 * Creates the list of navigation items for the bottom bar.
 */

@Composable
private fun createNavBottomItems(): List<NavBottom> = remember {
    listOf(
        NavBottom(NavBottomRoute.Home.route, Icons.Default.Home, R.string.home),
        NavBottom(NavBottomRoute.Topic.route, Icons.Default.Search, R.string.topic),
        NavBottom(NavBottomRoute.Favorite.route, Icons.Default.Star, R.string.favorite),
        NavBottom(NavBottomRoute.History.route, Icons.AutoMirrored.Filled.List, R.string.history),
        NavBottom(NavBottomRoute.Settings.route, Icons.Default.Settings, R.string.settings),
    )

}

/**
 * Sets up a listener for changes in the bottom navigation destination.
 */
@Composable
private fun SetupBottomNavigationListener(
    navController: NavHostController,
    items: List<NavBottom>,
    onDestinationChanged: (NavBottom) -> Unit
) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        items.find { it.route == destination.route }?.let {
            onDestinationChanged(it)
        }
    }
}

/**
 * Handles the back press behavior for the dashboard.
 */
@Composable
private fun HandleBackPress(currentNavBottom: NavBottom) {
    val activity = LocalActivity.current
    BackHandler(enabled = true) {
        if (currentNavBottom.route == NavBottomRoute.Home.route) {
            activity?.finish()
        }
    }
}

/**
 * Composable for the Dashboard's top app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardAppBar(
    title: String,
    onNotificationClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        windowInsets = WindowInsets(0.dp),
        title = {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        actions = {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }
        }
    )
}

/**
 * Composable for the Dashboard's bottom navigation bar.
 */
@Composable
fun DashboardBottomBar(
    items: List<NavBottom>,
    selectedRoute: String,
    onItemSelected: (NavBottom) -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.extendedColors.background,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomNavItem(
                    item = item,
                    isSelected = item.route == selectedRoute,
                    onClick = { onItemSelected(item) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    item: NavBottom,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f)
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = stringResource(item.label),
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(item.label),
            color = contentColor,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashBoardPreview() {
    DashBoardContent(
        toastMessageController = rememberToastMessageController(),
        appNavController = rememberNavController(),
        navController = rememberNavController()
    )
}
