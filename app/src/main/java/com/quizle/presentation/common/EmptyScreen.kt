package com.quizle.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.extendedColors.background,
    contentColor: Color = MaterialTheme.extendedColors.onBackground,
    message: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            textAlign = TextAlign.Center,
            color = contentColor
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun EmptyScreenPreview() {
    QuizleTheme {
        EmptyScreen(
            message = "No Question Available"
        )
    }
}

