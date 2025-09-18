package com.quizle.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.R
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors
import kotlin.math.max

@Composable
fun TextFieldBox(
    modifier: Modifier = Modifier,
    value: String,
    shape: Shape = RoundedCornerShape(8.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    hint: String? = null,
    hintColor: Color = Color.Gray,
    textColor: Color = MaterialTheme.extendedColors.primary,
    cursorColor: Color = MaterialTheme.extendedColors.primary,
    focusContainerColor: Color = MaterialTheme.extendedColors.onSurface,
    unFocusContainerColor: Color = MaterialTheme.extendedColors.onSurface.copy(alpha = 0.8f),
    keyboardType: KeyboardType = KeyboardType.Text,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    error: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Unspecified
) {
    Column {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            shape = shape,
            value = value,
            keyboardActions = keyboardActions,
            enabled = enabled,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onValueChange = {
                onValueChange(it)
            },
            prefix = prefix,
            suffix = suffix,
            placeholder = {
                if (hint != null) {
                    Text(
                        text = hint,
                        color = hintColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                errorTextColor = textColor,
                unfocusedContainerColor = unFocusContainerColor,
                focusedContainerColor = focusContainerColor,
                errorContainerColor = focusContainerColor,
                disabledContainerColor = focusContainerColor,
                disabledTextColor = textColor,
                disabledLeadingIconColor = textColor,
                disabledTrailingIconColor = textColor,
                disabledBorderColor = focusContainerColor,
                disabledPlaceholderColor = hintColor,
                disabledSuffixColor = textColor,
                disabledPrefixColor = textColor,
                cursorColor = cursorColor,
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


@Composable
fun TextFieldPassword(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String? = null,
    hintColor: Color = MaterialTheme.extendedColors.primary.copy(0.5f),
    textColor: Color = MaterialTheme.extendedColors.primary,
    cursorColor: Color = MaterialTheme.extendedColors.primary,
    focusContainerColor: Color = MaterialTheme.extendedColors.onSurface,
    unFocusContainerColor: Color = MaterialTheme.extendedColors.onSurface.copy(alpha = 0.8f),
    error: String? = null
){
    var passwordVisible by remember { mutableStateOf(false) }
    var ic by remember { mutableIntStateOf(R.drawable.ic_visible) }

    LaunchedEffect(passwordVisible) {
        ic = if (passwordVisible) R.drawable.ic_visible else R.drawable.ic_invisible
    }

    TextFieldBox(
        modifier = modifier,
        value = value,
        onValueChange = {onValueChange(it)},
        cursorColor = cursorColor,
        focusContainerColor = focusContainerColor,
        unFocusContainerColor = unFocusContainerColor,
        hint = hint,
        hintColor = hintColor,
        keyboardType = KeyboardType.Password,
        error = error,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(ic), contentDescription = description,
                    tint = textColor
                )
            }
        }
    )


}

//@Composable
//fun TextFieldPassword(
//    modifier: Modifier = Modifier,
//    onValueChange: (String) -> Unit,
//    value: String,
//    textPlaceHolder: String? = null,
//    textPlaceHolderColor: Color = MaterialTheme.extendedColors.primary.copy(0.5f),
//    textColor: Color = MaterialTheme.extendedColors.primary,
//    cursorColor: Color = MaterialTheme.extendedColors.primary,
//    containerColor: Color = MaterialTheme.extendedColors.onSurface,
//    error: String? = null
//) {
//    var passwordVisible by remember { mutableStateOf(false) }
//    val ic = if (passwordVisible) R.drawable.ic_visible else R.drawable.ic_invisible
//
//    Column(
//        modifier = modifier.fillMaxWidth()
//    ) {
//        OutlinedTextField(
//            modifier = Modifier.fillMaxWidth(),
//            value = value,
//            onValueChange = { onValueChange(it) },
//            isError = !error.isNullOrEmpty(),
//            placeholder = {
//                if (textPlaceHolder != null) {
//                    Text(
//                        text = textPlaceHolder,
//                        color = textPlaceHolderColor
//                    )
//                }
//            },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedTextColor = textColor,
//                unfocusedTextColor = textColor,
//                errorTextColor = textColor,
//                unfocusedContainerColor = containerColor,
//                focusedContainerColor = containerColor,
//                errorContainerColor = containerColor,
//                cursorColor = cursorColor,
//            ),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                val description = if (passwordVisible) "Hide password" else "Show password"
//                IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                    Icon(
//                        painter = painterResource(ic), contentDescription = description,
//                        tint = textColor
//                    )
//                }
//            }
//        )
//
//        if (!error.isNullOrEmpty()) {
//            Text(
//                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
//                text = error,
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.extendedColors.error
//            )
//        }
//    }
//}



@Preview(name = "Password Field - Light Theme")
@Composable
private fun TextFieldPasswordPreview() {
    QuizleTheme(darkTheme  = false) {
        TextFieldPassword(
            value = "",
            onValueChange = {},
            error = null,
            hint = "Password"

        )
    }
}



@Preview(name = "TextField Box Light Mode")
@Composable
private fun TextFieldBoxPreview() {
    QuizleTheme(darkTheme = false) {
        TextFieldBox(
            value = "",
            onValueChange = {},
            hint = "Email",
            keyboardType = KeyboardType.Email,
            error = "Invalid Email",
        )
    }
}
