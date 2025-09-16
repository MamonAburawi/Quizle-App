package com.quizle.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors


@Composable
fun ConfirmationDialog(
    title: String,
    subTitle: String,
    positiveBtnText: String,
    negativeBtnText: String? = null,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(
                        text = positiveBtnText,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.extendedColors.onSurfaceColor
                    )
                }
            },
            dismissButton = {
                if (negativeBtnText != null) {
                    TextButton(onClick = onDismissRequest) {
                        Text(
                            text = negativeBtnText,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.extendedColors.onSurfaceColor
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.extendedColors.surfaceColor,
            titleContentColor = MaterialTheme.extendedColors.onSurfaceColor,
            textContentColor = MaterialTheme.extendedColors.onSurfaceColor.copy(alpha = 0.6f)
        )
    }
}


@Preview(name = "Dialog - Light Theme")
@Composable
private fun ConfirmationDialogLightPreview() {
    QuizleTheme(darkTheme  = false) {
        // A Box is used to provide a background for the preview
        Box(modifier = Modifier.padding(16.dp)) {
            ConfirmationDialog(
                title = "Submit Quiz",
                subTitle = "Are you sure you want to submit the quiz?",
                isOpen = true,
                onDismissRequest = {},
                onConfirm = {},
                positiveBtnText = "Submit",
                negativeBtnText = "Cancel"
            )
        }
    }
}

@Preview(name = "Dialog - Dark Theme")
@Composable
private fun ConfirmationDialogDarkPreview() {
    QuizleTheme(darkTheme  = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            ConfirmationDialog(
                title = "Submit Quiz",
                subTitle = "Are you sure you want to submit the quiz?",
                isOpen = true,
                onDismissRequest = {},
                onConfirm = {},
                positiveBtnText = "Submit",
                negativeBtnText = "Cancel"
            )
        }
    }
}