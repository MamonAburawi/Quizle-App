package com.quizle.presentation.screens.notifications


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.quizle.presentation.theme.extendedColors


@Composable
fun NotificationScreen() {
    NotificationContent()
}

@Composable
private fun NotificationContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColors.backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Notification",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.extendedColors.onBackgroundColor
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    NotificationContent()
}