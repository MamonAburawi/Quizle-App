package com.quizle.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.quizle.R
import com.quizle.presentation.common.TextFieldPassword
import com.quizle.presentation.common.TextFieldBox
import com.quizle.presentation.common.LoadingButton
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.common.PressableText
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.navigateToDashboard
import com.quizle.presentation.navigation.navigateToSignUp
import com.quizle.presentation.theme.QuizleTheme // Assuming you have a theme file
import com.quizle.presentation.theme.extendedColors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LoginScreen(
    navController: NavController,
    state: LoginState,
    toastMessageController: ToastMessageController,
    onAction: (LoginAction) -> Unit,
    event: Flow<LoginEvent>,
) {
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
    LaunchedEffect(key1 = Unit) {
        event.collect { event ->
            when (event) {
                is LoginEvent.NavigateToDashboardScreen -> navigateToDashboard()
                is LoginEvent.ShowToastMessage -> onToastMessage(event.message, event.type)
                is LoginEvent.NavigateToSignUpScreen -> navigateToSignUp()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()), // Makes the screen scrollable on smaller devices
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- Header Section ---
            HeaderSection()

            Spacer(modifier = Modifier.height(48.dp))

            // --- Form Section ---
            EmailField(
                value = state.email,
                onValueChange = { onAction(LoginAction.EmailChanged(it)) },
                error = state.emailFieldErrorMessage
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                value = state.password,
                onValueChange = { onAction(LoginAction.PasswordChanged(it)) },
                error = state.passwordFieldErrorMessage
            )

            Spacer(modifier = Modifier.height(24.dp))


            OptionsSection(
                rememberMeChecked = state.rememberMe,
                onRememberMeChanged = { onAction(LoginAction.RememberMeChanged(it)) },
                onForgotPasswordClicked = { }
            )

            Spacer(modifier = Modifier.height(32.dp))


            ActionsSection(
                isLoading = state.isLoading,
                onLoginClick = { onAction(LoginAction.LoginButtonClicked) },
                onSignUpClick = { onAction(LoginAction.SignUpButtonClicked) }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}



@Composable
private fun HeaderSection() {
    val appIcon = painterResource(id = R.drawable.ic_app_transparent)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = appIcon,
            contentDescription = "App Icon",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.welcome_back),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    TextFieldBox(
        value = value,
        onValueChange = onValueChange,
        textPlaceHolder = stringResource(R.string.email),
        error = error,
        keyboardType = KeyboardType.Email
    )
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    TextFieldPassword(
        value = value,
        onValueChange = onValueChange,
        textPlaceHolder = stringResource(R.string.password),
        error = error
    )
}

@Composable
private fun OptionsSection(
    rememberMeChecked: Boolean,
    onRememberMeChanged: (Boolean) -> Unit,
    onForgotPasswordClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMeChecked,
                onCheckedChange = onRememberMeChanged,
            )
            Text(
                text = stringResource(R.string.remember_me),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.extendedColors.onBackground
            )
        }
        PressableText(
            text = stringResource(R.string.forgot_your_password),
            onClick = onForgotPasswordClicked,
            fontWeight = FontWeight.Bold,
            defaultColor = MaterialTheme.extendedColors.onBackground
        )
    }
}

@Composable
private fun ActionsSection(
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.login),
            isLoading = isLoading,
            onClick = onLoginClick
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.you_don_t_have_an_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.extendedColors.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            PressableText(
                text = stringResource(R.string.signup),
                onClick = onSignUpClick,
                fontWeight = FontWeight.Bold,
                defaultColor = MaterialTheme.extendedColors.onSurface
            )
        }
    }
}

// --- Preview ---

@Preview(showBackground = true, name = "Login Screen Light")
@Composable
fun LoginPreview() {
    val state = LoginState(
        email = "example@email.com",
        password = "password123",
        rememberMe = true,
        isLoading = false,
        error = null,
        passwordFieldErrorMessage = null,
        emailFieldErrorMessage = null
    )

    QuizleTheme {
        LoginContent(
            state = state,
            onAction = {},
            event = emptyFlow(),
            onToastMessage = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "Login Screen Dark")
@Composable
fun LoginPreviewDark() {
    val state = LoginState(
        email = "example@email.com",
        password = "password123",
        rememberMe = true,
        isLoading = false,
        emailFieldErrorMessage = "Invalid email format" // Previewing an error state
    )
    QuizleTheme(darkTheme = true) {
        LoginContent(
            state = state,
            onAction = {},
            event = emptyFlow(),
            onToastMessage = { _, _ -> }
        )
    }
}