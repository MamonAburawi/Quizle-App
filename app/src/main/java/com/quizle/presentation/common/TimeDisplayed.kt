package com.quizle.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.R
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors

@Composable
fun TimeDisplay(
    modifier: Modifier = Modifier,
    timeInMinutes: Int,
    // NEW: Defaults are now from the MaterialTheme color scheme
    containerColor: Color = MaterialTheme.extendedColors.error,
    contentColor: Color = MaterialTheme.extendedColors.onSurface
) {
    val displayString = when {
        timeInMinutes < 1 -> stringResource(R.string.seconds_short, timeInMinutes * 60)
        timeInMinutes < 60 -> stringResource(R.string.minutes_short, timeInMinutes)
        else -> {
            val hours = timeInMinutes / 60
            val minutes = timeInMinutes % 60
            if (minutes == 0) {
                stringResource(R.string.hours_short, hours)
            } else {
                stringResource(R.string.hours_minutes_short, hours, minutes)
            }
        }
    }

    Text(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = displayString,
        color = contentColor,
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}


// --- PREVIEWS ---

@Preview(name = "Time Display - Light Theme", showBackground = true)
@Composable
private fun TimeDisplayLightPreview() {
    QuizleTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimeDisplay(timeInMinutes = 45)  // Displays "45 min"
            TimeDisplay(timeInMinutes = 60)  // Displays "1 h"
            TimeDisplay(timeInMinutes = 110) // Displays "1 h 50 min"
        }
    }
}

@Preview(name = "Time Display - Dark Theme", showBackground = true)
@Composable
private fun TimeDisplayDarkPreview() {
    QuizleTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimeDisplay(timeInMinutes = 45)  // Displays "45 min"
            TimeDisplay(timeInMinutes = 60)  // Displays "1 h"
            TimeDisplay(timeInMinutes = 110) // Displays "1 h 50 min"
        }
    }
}