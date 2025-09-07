package com.quizle.presentation.screens.sign_up

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.quizle.R
import com.quizle.presentation.common.BordTextField
import com.quizle.presentation.common.BordPasswordTextField
import com.quizle.presentation.common.LoadingButton
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.common.PressableText
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.common.rememberToastMessageController
import com.quizle.presentation.navigation.navigateToDashboard
import com.quizle.presentation.navigation.navigateToLogin

import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SignUpScreen(
    navController: NavController,
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    event: Flow<SignUpEvent>,
    toastMessageController: ToastMessageController
){

    // todo: add gender radio button
    SignUpContent(
        state = state,
        onAction = onAction,
        event = event,
        navigateToDashboard = {
            navController.navigateToDashboard()
        },
        navigateToLogin = {
            navController.navigateToLogin()
        },
        onToastMessage = { message, type ->
            toastMessageController.showToast(message, type)
        }
    )

}


@Composable
fun SignUpContent(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    event: Flow<SignUpEvent>,
    navigateToDashboard: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    onToastMessage: (String, MessageType) -> Unit,
    ) {
    // Replace with your actual icon resource
    val appIcon = painterResource(id = R.drawable.ic_app_transparent) // Example icon
    val toastController = rememberToastMessageController(
        alignment = Alignment.Top
    )

    LaunchedEffect(key1 = Unit) {
        event.collect{ event ->
            when(event){
                SignUpEvent.NavigateToDashboardScreen -> {
                    navigateToDashboard()
                }
                is SignUpEvent.NavigateToLoginScreen -> {
                    navigateToLogin()
                }
                is SignUpEvent.ShowToastMessage -> {
                    val message = event.message
                    val toastType = event.type
                    onToastMessage(message,toastType)
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
            value = state.name,
            onValueChange = {
                onAction(SignUpAction.NameChanged(it))
            },
            label = stringResource(R.string.your_name),
            error = state.nameFieldErrorMessage,
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Email TextField
        BordTextField(
            value = state.email,
            onValueChange = {
                onAction(SignUpAction.EmailChanged(it))
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
                onAction(SignUpAction.PasswordChanged(it))
            },
            error = state.passwordFieldErrorMessage
        )
        Spacer(modifier = Modifier.height(15.dp))

        // Password TextField
        BordPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.confirmPassword,
            label = stringResource(R.string.confirm_password),
            onValueChange = {
                onAction(SignUpAction.ConfirmPasswordChanged(it))
            },
            error = state.confirmPasswordErrorMessage
        )

        Spacer(modifier = Modifier.height(40.dp))

        LoadingButton(
            text = stringResource(R.string.signup),
            fontWeight = FontWeight.Bold,
            isLoading = state.isLoading,
            onClick = {
                onAction(SignUpAction.SignUpButtonClicked)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "You don't have an account",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
            Spacer(modifier = Modifier.width(5.dp))
            PressableText(
                text = stringResource(R.string.login),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                onClick = {
                    onAction(SignUpAction.LoginButtonClicked)
                }
            )
        }



    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val state = SignUpState(
        name = "Ahmed",
        email = "william.henry.harrison@example-pet-store.com",
        password = "password123",
        confirmPassword = "password123",
        isLoading = false,
        error = null,
        passwordFieldErrorMessage = null,
        emailFieldErrorMessage = null,
        confirmPasswordErrorMessage = null
    )

    SignUpContent(
        state = state,
        onAction = {},
        event = emptyFlow(),
        onToastMessage = { _, _ ->}
    )
}
