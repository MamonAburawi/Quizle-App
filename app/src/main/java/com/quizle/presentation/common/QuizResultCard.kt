package com.quizle.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizle.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QuizResultCard(
    createdAt: Long,
    totalQuestions: Int,
    correctAnswersCount: Int,
    topicTitle: String,
    topicSubTitle: String,
    enableAnimation: Boolean = true
) {
    val progress = correctAnswersCount.toFloat() / totalQuestions.toFloat()
    val scoreColor = when {
        progress >= 0.8 -> Color(0xFF4CAF50) // Green
        progress >= 0.5 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }

    // Animate the progress if enableAnimation is true
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = if (enableAnimation) tween(durationMillis = 1000) else tween(durationMillis = 0),
        label = "quiz_progress_animation"
    )

    // Format the timestamp
    val dateFormat = SimpleDateFormat("MMM dd 'at' h:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(createdAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Quiz Title and Subtitle
            Text(
                text = topicTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = topicSubTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Score Circle with Progress Indicator
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(120.dp),
                    color = scoreColor,
                    strokeWidth = 10.dp,
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$correctAnswersCount",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        color = scoreColor
                    )
                    Text(
                        text = "/$totalQuestions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            // Creation Time
            Text(
                text = stringResource(R.string.completion_date_message, formattedDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 15.dp)
            )

            // Separator
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            // Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.correct),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$correctAnswersCount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.total),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$totalQuestions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun QuizResultCardPreview() {
    QuizResultCard(
        createdAt = 162,
        totalQuestions = 20,
        correctAnswersCount = 20,
        topicTitle = "Quiz Title",
        topicSubTitle = "Quiz Subtitle",
        enableAnimation = true
    )
}