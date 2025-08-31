package com.quizle.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.quizle.R


@Composable
fun TimeDisplay(
    timeInMinutes: Int,
    containerColor: Color = Color.Red,
    contentColor: Color = Color.White
) {

    val timeInSeconds = (timeInMinutes * 60)
    val totalMinutes = timeInMinutes

    val displayString = when {
        // If time is less than 60 seconds (1 minute)
        totalMinutes < 1 -> stringResource(R.string.seconds_short, timeInSeconds)
        // If time is less than 60 minutes
        totalMinutes <= 60 -> stringResource(R.string.minutes_short, totalMinutes)
        // If time is 60 minutes or more
        else -> {
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60
            if (hours > 1 && minutes == 0){
                stringResource(R.string.hours_short, hours)
            }else {
                stringResource(R.string.hours_minutes_short, hours, minutes)
            }

        }
    }

    // Your Text composable
    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        text = displayString,
        color = contentColor,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}



@Preview
@Composable
fun TimeDisplayedPreview() {
    TimeDisplay(timeInMinutes = 45)
}