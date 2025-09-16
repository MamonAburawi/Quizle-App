package com.quizle.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors


@Composable
fun BordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            label = {
                if (label != null) {
                    Text(label)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.extendedColors.primaryColor,
                unfocusedTextColor = MaterialTheme.extendedColors.primaryColor,

            ),
            isError = error != null
        )

        if (!error.isNullOrEmpty()){
            Text(
                modifier = Modifier.padding(10.dp),
                text = error,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

}


@Preview(name = "BordTextField Light Mode", showBackground = true)
@Composable
fun BordTextFieldLightPreview() {
    QuizleTheme(darkTheme = false) {
        BordTextField(
            value = "",
            onValueChange = {},
            label = "Email",
            keyboardType = KeyboardType.Email,
            error = "Invalid Email"
        )
    }
}

@Preview(name = "BordTextField Dark Mode", showBackground = true)
@Composable
fun BordTextFieldDarkPreview() {
    QuizleTheme(darkTheme = true) {
        BordTextField(
            value = "",
            onValueChange = {},
            label = "Email",
            keyboardType = KeyboardType.Email,
            error = "Invalid Email"
        )
    }
}