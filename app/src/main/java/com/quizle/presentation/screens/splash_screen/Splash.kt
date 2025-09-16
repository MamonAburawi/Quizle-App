package com.quizle.presentation.screens.splash_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.presentation.common.AnimatedLoadingDotsText
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.navigateToDashboard
import com.quizle.presentation.navigation.navigateToLogin
import com.quizle.presentation.theme.extendedColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SplashScreen(
    navController: NavHostController,
    state: SplashState,
    toastMessageController: ToastMessageController,
    event: Flow<SplashEvent>
) {

    SplashScreenContent(
        state = state,
        navigateToDashboard = {
            navController.navigateToDashboard()
        },
        navigateToLogin = {
            navController.navigateToLogin()
        },
        event = event
    )

}

@Composable
private fun SplashScreenContent(
    navigateToDashboard: () -> Unit,
    navigateToLogin: () -> Unit,
    event: Flow<SplashEvent>,
    state: SplashState
) {

    LaunchedEffect(key1 = Unit) {
        event.collect {
            when (it) {
                is SplashEvent.NavigateToDashboardScreen -> {
                    navigateToDashboard()
                }
                is SplashEvent.NavigateToLoginScreen -> {
                    navigateToLogin()
                }
                is SplashEvent.ShowToastMessage -> {

                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
       Column(
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           Image(
               modifier = Modifier
                   .size(120.dp)
                   .clip(CircleShape),
               painter = painterResource(R.drawable.ic_app_transparent),
               contentDescription = "Icon Splash",
           )
           Spacer(modifier = Modifier.height(50.dp))
           AnimatedLoadingDotsText(
               text = stringResource(R.string.loading_please_wait),
               color = MaterialTheme.extendedColors.primaryColor,
           )
       }
    }

}






@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreenContent(
        navigateToDashboard = {},
        navigateToLogin = {},
        event = emptyFlow(),
        state = SplashState()
    )
}