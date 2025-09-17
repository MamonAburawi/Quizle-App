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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.R
import com.quizle.presentation.common.TextFieldBox
import com.quizle.presentation.common.TextFieldPassword
import com.quizle.presentation.common.PrimaryButton
import com.quizle.presentation.common.PressableText
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.navigateToDashboard
import com.quizle.presentation.navigation.navigateToLogin
import com.quizle.presentation.screens.login.LoginAction
import com.quizle.presentation.theme.Color
import com.quizle.presentation.theme.extendedColors

@Composable
fun SignUpScreen(
    navController: NavController,
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    event: Flow<SignUpEvent>,
    toastMessageController: ToastMessageController
){


    LaunchedEffect(key1 = Unit) {
        event.collect{ event ->
            when(event){
                SignUpEvent.NavigateToDashboardScreen -> {
                    navController.navigateToDashboard()
                }
                is SignUpEvent.NavigateToLoginScreen -> {
                    navController.navigateToLogin()
                }
                is SignUpEvent.ShowToastMessage -> {
                    val message = event.message
                    val toastType = event.type
                    toastMessageController.showToast(message, toastType)
                }

            }
        }
    }

    SignUpContent(
        state = state,
        onLoginClicked = { onAction(SignUpAction.LoginButtonClicked) },
        onSignUpClicked = {onAction(SignUpAction.SignUpButtonClicked)},
        onNameChange = { onAction(SignUpAction.NameChanged(it))},
        onEmailChange = { onAction(SignUpAction.EmailChanged(it))},
        onPassChange = { onAction(SignUpAction.PasswordChanged(it)) },
        onConfPassChange = { onAction(SignUpAction.ConfirmPasswordChanged(it)) },
    )

}


@Composable
fun SignUpContent(
    state: SignUpState,
    onLoginClicked: () -> Unit,
    onSignUpClicked: ()-> Unit,
    onNameChange:(String) -> Unit,
    onEmailChange:(String) -> Unit,
    onPassChange:(String) -> Unit,
    onConfPassChange:(String) -> Unit

)
    {
    // Replace with your actual icon resource
    val appIcon = painterResource(id = R.drawable.ic_app_transparent) // Example icon

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColors.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = appIcon,
            contentDescription = "App Icon",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.register_new_info),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.extendedColors.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextFieldBox(
            value = state.name,
            onValueChange = { onNameChange(it) },
            hint = stringResource(R.string.your_name),
            error = state.nameFieldErrorMessage,
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextFieldBox(
            value = state.email,
            onValueChange = { onEmailChange(it) },
            hint = stringResource(R.string.email),
            error = state.emailFieldErrorMessage,
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldPassword(
            value = state.password,
            onValueChange = { onPassChange(it) },
            hint = stringResource(R.string.password),
            error = state.passwordFieldErrorMessage
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextFieldPassword(
            value = state.confirmPassword,
            onValueChange = { onConfPassChange(it) },
            hint = stringResource(R.string.confirm_password),
            error = state.confirmPasswordErrorMessage
        )
        Spacer(modifier = Modifier.height(40.dp))
        PrimaryButton(
            text = stringResource(R.string.signup),
            fontWeight = FontWeight.Bold,
            isLoading = state.isLoading,
            onClick = onSignUpClicked
        )

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.you_don_t_have_an_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.extendedColors.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            PressableText(
                text = stringResource(R.string.login),
                onClick = onLoginClicked,
                fontWeight = FontWeight.Bold,
                defaultColor = MaterialTheme.extendedColors.onSurface
            )
        }


    }
}


@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    val state = SignUpState(
        name = "Ahmed",
        email = "william.henry.harrison@example-pet-store.com",
        password = "password123",
        confirmPassword = "password123",
        isLoading = false,
        error = null,
        passwordFieldErrorMessage = null,
        emailFieldErrorMessage = null,
        confirmPasswordErrorMessage = null,
        nameFieldErrorMessage = null
    )

    SignUpContent(
        state = state,
        onLoginClicked = {},
        onNameChange = {},
        onEmailChange = {},
        onPassChange = {},
        onConfPassChange = {},
        onSignUpClicked = {}
    )
}
