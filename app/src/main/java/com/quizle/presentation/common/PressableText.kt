package com.quizle.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit

@Composable
fun PressableText(
    text: String,
    modifier: Modifier = Modifier,
    defaultColor: Color = MaterialTheme.colorScheme.primary,
    pressedColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), // Slightly darker for feedback
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    fontWeight: FontWeight = FontWeight.Normal,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var currentColor by remember { mutableStateOf(defaultColor) }

    // Collect press interactions to update the color
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        currentColor = if (isPressed) pressedColor else defaultColor
    }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null, // This removes the visual ripple effect
                onClick = onClick
            )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = currentColor
        )
    }
}

// Helper extension function to observe press state
@Composable
private fun MutableInteractionSource.collectIsPressedAsState(): androidx.compose.runtime.State<Boolean> {
    val isPressed = remember { mutableStateOf(false) }
    val currentIsPressed by rememberUpdatedState(isPressed.value)

    LaunchedEffect(this) {
        this@collectIsPressedAsState.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> isPressed.value = true
                is PressInteraction.Release -> isPressed.value = false
                is PressInteraction.Cancel -> isPressed.value = false
            }
        }
    }
    return rememberUpdatedState(currentIsPressed)
}


@Preview
@Composable
fun PressableTextPreview() {
    PressableText(
        text = "Press Me",
        onClick = {
            // Handle click action here
        }
    )
}