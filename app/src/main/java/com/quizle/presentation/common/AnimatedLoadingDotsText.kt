package com.quizle.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.quizle.presentation.theme.extendedColors
import kotlinx.coroutines.delay

@Composable
fun AnimatedLoadingDotsText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.extendedColors.onBackgroundColor,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize
) {
    var dotCount by remember { mutableIntStateOf(0) }

    // Launch a coroutine to update the dot count periodically
    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // Wait for 500 milliseconds
            dotCount = (dotCount + 1) % 4 // Cycle through 0, 1, 2, 3 dots
        }
    }

    val dots = remember(dotCount) {
        (0 until dotCount).joinToString("") { "." }
    }

    Text(
        modifier = modifier,
        text = "$text$dots",
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color
    )
}



@Preview
@Composable
fun AnimatedLoadingDotsTextPreview() {
    AnimatedLoadingDotsText(
        text = "Loading",
        color = Color.Black,
        fontWeight = FontWeight.Normal,
        fontSize = MaterialTheme.typography.titleLarge.fontSize
    )
}