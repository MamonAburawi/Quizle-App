package com.quizle.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    message: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            textAlign = TextAlign.Center,
        )

    }

}


@Preview(showBackground = true)
@Composable
private fun EmptyScreenPreview() {
    EmptyScreen(
        message = "No Question Available"
    )
}

