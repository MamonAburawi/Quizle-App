package com.quizle.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.R
import com.quizle.presentation.theme.QuizleTheme


@Composable
fun BordPasswordTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    value: String,
    label: String = "Password",
    error: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val ic = if (passwordVisible) R.drawable.ic_visible else R.drawable.ic_invisible

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { onValueChange(it) },
            isError = !error.isNullOrEmpty(),
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = painterResource(ic), contentDescription = description)
                }
            }
        )

        if (!error.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}



@Preview(name = "Password Field - Light Theme", showBackground = true)
@Composable
private fun PasswordOutlineTextFieldLightPreview() {
    QuizleTheme(darkTheme  = false) {
        BordPasswordTextField(
            value = "123456",
            onValueChange = {},
            error = null
        )
    }
}

@Preview(name = "Password Field - Dark Theme", showBackground = true)
@Composable
private fun PasswordOutlineTextFieldDarkPreview() {
    QuizleTheme(darkTheme  = true) {
        BordPasswordTextField(
            value = "123456",
            onValueChange = {},
            error = null
        )
    }
}

@Preview(name = "Password Field with Error - Light Theme", showBackground = true)
@Composable
private fun PasswordOutlineTextFieldErrorLightPreview() {
    QuizleTheme(darkTheme  = false) {
        BordPasswordTextField(
            value = "123",
            onValueChange = {},
            error = "Password is too short"
        )
    }
}

@Preview(name = "Password Field with Error - Dark Theme", showBackground = true)
@Composable
private fun PasswordOutlineTextFieldErrorDarkPreview() {
    QuizleTheme(darkTheme  = true) {
        BordPasswordTextField(
            value = "123",
            onValueChange = {},
            error = "Password is too short"
        )
    }
}