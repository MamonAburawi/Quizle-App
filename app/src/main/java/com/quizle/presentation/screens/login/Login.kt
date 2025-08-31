package com.quizle.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.quizle.R
import com.quizle.presentation.common.BordTextField
import com.quizle.presentation.common.BordPasswordTextField
import com.quizle.presentation.common.LoadingButton
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.common.PressableText
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.common.rememberToastMessageController
import com.quizle.presentation.navigation.navigateToDashboard
import com.quizle.presentation.navigation.navigateToSignUp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LoginScreen(
    navController: NavController,
    state: LoginState,
    toastMessageController: ToastMessageController,
    onAction: (LoginAction) -> Unit,
    event: Flow<LoginEvent>,
){

    LoginContent(
        state = state,
        onAction = onAction,
        event = event,
        navigateToDashboard = {
            navController.navigateToDashboard()
        },
        navigateToSignUp = {
            navController.navigateToSignUp()
        },
        onToastMessage = { message, type ->
            toastMessageController.showToast(message, type)
        }
    )

}


@Composable
fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    event: Flow<LoginEvent>,
    navigateToDashboard: () -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    onToastMessage: (String, MessageType) -> Unit,


    ) {
    // Replace with your actual icon resource
    val appIcon = painterResource(id = R.drawable.ic_app_transparent) // Example icon


    LaunchedEffect(key1 = Unit) {
        event.collect{ event ->
            when(event){
                is LoginEvent.NavigateToDashboardScreen ->{
                    navigateToDashboard()
                }
                is LoginEvent.ShowToastMessage -> {
                    val message = event.message
                    val toastType = event.type
                    onToastMessage(message,toastType)
                    // show toast message
                }
                is LoginEvent.NavigateToSignUpScreen ->{
                    navigateToSignUp()
                }

            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Centered Icon
        Image(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            painter = appIcon,
            contentDescription = "App Icon",

            )

        Spacer(modifier = Modifier.height(32.dp))

        // Email TextField
        BordTextField(
            value = state.email,
            onValueChange = {
                onAction(LoginAction.EmailChanged(it))
            },
            label = stringResource(R.string.email),
            error = state.emailFieldErrorMessage,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        BordPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            label = stringResource(R.string.password),
            onValueChange = {
                onAction(LoginAction.PasswordChanged(it))
            },
            error = state.passwordFieldErrorMessage
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = state.rememberMe,
                    onCheckedChange = { newState ->
                        onAction(LoginAction.RememberMeChanged(newState))
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.remember_me),
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string._3_months),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }


            }
            PressableText(
                text = stringResource(R.string.forgot_your_password),
                onClick = {

                },
                fontWeight = FontWeight.Normal,
                defaultColor = Color.Black,
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            )

        }

        Spacer(modifier = Modifier.height(40.dp))

        LoadingButton(
            text = stringResource(R.string.login),
            fontWeight = FontWeight.Bold,
            isLoading = state.isLoading,
            onClick = {
                onAction(LoginAction.LoginButtonClicked)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.you_don_t_have_an_account),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
            Spacer(modifier = Modifier.width(5.dp))
            PressableText(
                text = stringResource(R.string.signup),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                onClick = {
                    onAction(LoginAction.SignUpButtonClicked)
                }
            )
        }



    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val state = LoginState(
        email = "william.henry.harrison@example-pet-store.com",
        password = "password123",
        rememberMe = true,
        isLoading = false,
        error = null,
        passwordFieldErrorMessage = null,
        emailFieldErrorMessage = null
    )

    LoginContent(
        state = state,
        onAction = {},
        event = emptyFlow(),
        onToastMessage = { _, _ ->}
    )
}