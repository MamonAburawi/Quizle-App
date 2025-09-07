package com.quizle.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class MessageType(
    val backgroundColor: Color,
    val contentColor: Color,
    val icon: ImageVector
) {
    data object Success : MessageType(Color(0xFF4CAF50), Color.White, Icons.Default.CheckCircle)
    data object Info : MessageType(Color(0xFF2196F3), Color.White, Icons.Default.Info)
    data object Warning : MessageType(Color(0xFFFFC107), Color.Black, Icons.Default.Warning)
    data object Error : MessageType(Color(0xFFF44336), Color.White, Icons.Default.Close)
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
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = type.backgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = type.icon,
                    contentDescription = null, // Content description for accessibility
                    tint = type.contentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = message,
                    color = type.contentColor,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f) // Allow text to take remaining space
                )
            }
        }
    }
}


@Composable
fun ToastHost(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.Top
) {
    SnackbarHost(
        hostState = snackBarHostState,
        modifier = modifier
            .fillMaxWidth() // Ensure it spans the width
            .wrapContentHeight(align = alignment) // Align to the top
            .padding(top = 16.dp, start = 16.dp, end = 16.dp) // Add padding
    ) { snackBarData ->
        // Check if the visuals are our custom type and display our custom toast
        (snackBarData.visuals as? CustomSnackBarVisuals)?.let { customVisuals ->
            CustomToastMessage(
                message = customVisuals.message,
                type = customVisuals.type,
                modifier = Modifier.fillMaxWidth()
            )
        } ?: run {
            // Fallback for default Snackbars if you ever use them directly
            Snackbar(snackbarData = snackBarData)
        }
    }
}


@Composable
fun rememberToastMessageController(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    alignment: Alignment.Vertical = Alignment.Top
): ToastMessageController {
    val controller = remember {
        ToastMessageController(snackBarHostState, coroutineScope)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        ToastHost(
            alignment = alignment,
            snackBarHostState = snackBarHostState
        )
    }
    return controller
}


class ToastMessageController(
    private val snackBarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {

    fun showToast(
        message: String,
        type: MessageType = MessageType.Info,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        onAction: (() -> Unit)? = null
    ) {
        coroutineScope.launch {
            val customVisuals = CustomSnackBarVisuals(
                message = message,
                type = type,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration
            )

            val result = snackBarHostState.showSnackbar(customVisuals)

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    onAction?.invoke()
                }
                SnackbarResult.Dismissed -> {

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessToastMessagePreview() {
    CustomToastMessage(
        message = "This is a success message",
        type = MessageType.Success
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorToastMessagePreview() {
    CustomToastMessage(
        message = "This is a error message",
        type = MessageType.Error
    )
}

@Preview(showBackground = true)
@Composable
fun WarningToastMessagePreview() {
    CustomToastMessage(
        message = "This is a warning message",
        type = MessageType.Warning
    )
}

@Preview(showBackground = true)
@Composable
fun InfoToastMessagePreview() {
    CustomToastMessage(
        message = "This is an info message",
        type = MessageType.Info
    )
}