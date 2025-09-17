package com.quizle.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.error
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.theme.success
import com.quizle.presentation.theme.warning
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QuizResultCard(
    modifier: Modifier = Modifier,
    createdAt: Long,
    totalQuestions: Int,
    correctAnswersCount: Int,
    topicTitle: String,
    topicSubTitle: String,
    enableAnimation: Boolean = true,
    // NEW: Card and score colors are now parameters with theme-based defaults
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.extendedColors.surface,
        contentColor = MaterialTheme.extendedColors.onSurface
    ),
    successColor: Color = Color.success,
    warningColor: Color = Color.warning,
    errorColor: Color = Color.error
) {
    val progress = correctAnswersCount.toFloat() / totalQuestions.toFloat()
    // NEW: Score color now uses theme-based semantic colors
    val scoreColor = when {
        progress >= 0.8 -> successColor
        progress >= 0.5 -> warningColor
        else -> errorColor
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = if (enableAnimation) tween(durationMillis = 1000) else tween(durationMillis = 0),
        label = "quiz_progress_animation"
    )

    val dateFormat = SimpleDateFormat("MMM dd 'at' h:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(createdAt))

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = colors
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = topicTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = topicSubTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.extendedColors.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(120.dp),
                    color = scoreColor,
                    strokeWidth = 10.dp,
                    // Use the standard track color for better theme adherence
                    trackColor = ProgressIndicatorDefaults.circularTrackColor,
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
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = stringResource(R.string.completion_date_message, formattedDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.extendedColors.onSurface,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ResultDetail(label = stringResource(R.string.correct), value = "$correctAnswersCount")
                ResultDetail(label = stringResource(R.string.total), value = "$totalQuestions")
            }
        }
    }
}

@Composable
private fun ResultDetail(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.extendedColors.onSurface,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.extendedColors.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}



@Preview(name = "High Score - Light Theme", showBackground = true)
@Composable
private fun HighScoreLightPreview() {
    QuizleTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            QuizResultCard(
                createdAt = System.currentTimeMillis(),
                totalQuestions = 20,
                correctAnswersCount = 18, // High score (>= 80%)
                topicTitle = "History",
                topicSubTitle = "Ancient Civilizations"
            )
        }
    }
}

@Preview(name = "Medium Score - Dark Theme", showBackground = true)
@Composable
private fun MediumScoreDarkPreview() {
    QuizleTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            QuizResultCard(
                createdAt = System.currentTimeMillis(),
                totalQuestions = 20,
                correctAnswersCount = 13, // Medium score (>= 50%)
                topicTitle = "Science",
                topicSubTitle = "Biology Basics"
            )
        }
    }
}

@Preview(name = "Low Score - Light Theme", showBackground = true)
@Composable
private fun LowScoreLightPreview() {
    QuizleTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            QuizResultCard(
                createdAt = System.currentTimeMillis(),
                totalQuestions = 20,
                correctAnswersCount = 5, // Low score (< 50%)
                topicTitle = "Mathematics",
                topicSubTitle = "Algebra I"
            )
        }
    }
}