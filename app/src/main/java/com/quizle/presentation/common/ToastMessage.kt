package com.quizle.presentation.common


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.error
import com.quizle.presentation.theme.info
import com.quizle.presentation.theme.success
import com.quizle.presentation.theme.warning



// NEW: MessageType no longer contains hardcoded colors.
sealed class MessageType(val icon: ImageVector) {
    data object Success : MessageType(Icons.Default.CheckCircle)
    data object Info : MessageType(Icons.Default.Info)
    data object Warning : MessageType(Icons.Default.Warning)
    data object Error : MessageType(Icons.Default.Close)
}

// Data class to hold the resolved theme colors for the toast.
private data class ToastColors(val containerColor: Color, val contentColor: Color)


@Composable
private fun MessageType.toToastColors(): ToastColors {
    return when (this) {
        is MessageType.Success -> ToastColors(
            containerColor = Color.success,
            contentColor = Color.White
        )
        is MessageType.Info -> ToastColors(
            containerColor = Color.info,
            contentColor = Color.White
        )
        is MessageType.Warning -> ToastColors(
            containerColor = Color.warning,
            contentColor = Color.White
        )
        is MessageType.Error -> ToastColors(
            containerColor = Color.error,
            contentColor = Color.White
        )
    }
}


class CustomSnackBarVisuals(
    override val message: String,
    val type: MessageType,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals

@Composable
fun CustomToastMessage(
    message: String,
    type: MessageType,
    modifier: Modifier = Modifier
) {
    // NEW: Colors are resolved here using the current theme context.
    val colors = type.toToastColors()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colors.containerColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = type.icon,
                contentDescription = null,
                tint = colors.contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = colors.contentColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// No changes needed for ToastHost, rememberToastMessageController, or ToastMessageController.
// They correctly pass the `MessageType` enum, and the colors are resolved later.
@Composable
fun ToastHost(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.Top
) {
    SnackbarHost(
        hostState = snackBarHostState,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(align = alignment)
            .padding(16.dp)
    ) { snackBarData ->
        (snackBarData.visuals as? CustomSnackBarVisuals)?.let { customVisuals ->
            CustomToastMessage(
                message = customVisuals.message,
                type = customVisuals.type
            )
        } ?: Snackbar(snackbarData = snackBarData)
    }
}


// --- PREVIEWS ---

@Preview(name = "Toasts - Light Theme", showBackground = true)
@Composable
private fun ToastsLightPreview() {
    QuizleTheme(darkTheme = false) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)) {
            CustomToastMessage(message = "This is a success message.", type = MessageType.Success)
            CustomToastMessage(message = "This is an info message.", type = MessageType.Info)
            CustomToastMessage(message = "This is a warning message.", type = MessageType.Warning)
            CustomToastMessage(message = "This is an error message.", type = MessageType.Error)
        }
    }
}

@Preview(name = "Toasts - Dark Theme", showBackground = true)
@Composable
private fun ToastsDarkPreview() {
    QuizleTheme(darkTheme = true) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)) {
            CustomToastMessage(message = "This is a success message.", type = MessageType.Success)
            CustomToastMessage(message = "This is an info message.", type = MessageType.Info)
            CustomToastMessage(message = "This is a warning message.", type = MessageType.Warning)
            CustomToastMessage(message = "This is an error message.", type = MessageType.Error)
        }
    }
}

// (The ToastHost, rememberToastMessageController, and ToastMessageController classes remain the same)