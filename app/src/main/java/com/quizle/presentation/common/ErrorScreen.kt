package com.quizle.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun ErrorScreen(
    modifier: Modifier = Modifier,
    error: String,
    background: Color = MaterialTheme.extendedColors.background,
    contentColor: Color = MaterialTheme.extendedColors.error,
    onRefresh: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            textAlign = TextAlign.Center,
            color = contentColor
        )
        IconButton(
            onClick = onRefresh
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    QuizleTheme {
        ErrorScreen(
            error = "Something went wrong",
            onRefresh = {}
        )
    }
}

