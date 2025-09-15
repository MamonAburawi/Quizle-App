package com.quizle.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.quizle.presentation.theme.QuizleTheme


@Composable
fun PressableText(
    text: String,
    modifier: Modifier = Modifier,
    defaultColor: Color = MaterialTheme.colorScheme.primary,
    pressedColor: Color = defaultColor.copy(alpha = 0.7f),
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    fontWeight: FontWeight = FontWeight.Normal,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Determine the color based on the press state
    val currentColor = if (isPressed) pressedColor else defaultColor

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Removes the visual ripple effect
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



@Preview(name = "Pressable Text Primary - Light", showBackground = true)
@Composable
private fun PressableTextPrimaryLightPreview() {
    QuizleTheme(darkTheme = false) {
        PressableText(
            text = "Press Me",
            onClick = {}
        )
    }
}

@Preview(name = "Pressable Text Primary - Dark", showBackground = true)
@Composable
private fun PressableTextPrimaryDarkPreview() {
    QuizleTheme(darkTheme = true) {
        PressableText(
            text = "Press Me",
            onClick = {}
        )
    }
}

