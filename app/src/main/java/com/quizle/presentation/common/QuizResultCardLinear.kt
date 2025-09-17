package com.quizle.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.R
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.error
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.theme.success
import com.quizle.presentation.theme.warning
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultCardLinear(
    modifier: Modifier = Modifier,
    createdAt: Long,
    totalQuestions: Int,
    correctAnswersCount: Int,
    topicTitle: String,
    topicSubTitle: String,
    onDeleteClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.extendedColors.surface,
    ),
    successColor: Color = Color.success,
    warningColor: Color = Color.warning,
    errorColor: Color = Color.error
) {
    var isExpanded by remember { mutableStateOf(false) }

    val progress = correctAnswersCount.toFloat() / totalQuestions.toFloat()
    // NEW: Score color now uses theme-based semantic colors
    val progressColor = when {
        progress >= 0.8 -> successColor
        progress >= 0.5 -> warningColor
        else -> errorColor
    }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(createdAt))

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = colors,
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = topicTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.extendedColors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = topicSubTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.extendedColors.onSurface.copy(alpha = 0.8f)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Correct answers",
                        tint = progressColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "$correctAnswersCount/$totalQuestions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = progressColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = progressColor,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.completion_date_message, formattedDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.extendedColors.onSurface
            )

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.extendedColors.error),
                        border = BorderStroke(1.dp, MaterialTheme.extendedColors.error)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Quiz")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.delete))
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Button(
                        onClick = onTryAgainClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.extendedColors.primary,
                            contentColor = MaterialTheme.extendedColors.textPrimary
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Try Again")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.try_again))
                    }
                }
            }
        }
    }
}


@Preview(name = "High Score - Light Theme", showBackground = true)
@Composable
private fun HighScoreLightPreview() {
    QuizleTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            QuizResultCardLinear(
                createdAt = System.currentTimeMillis(),
                totalQuestions = 20,
                correctAnswersCount = 18, // High score
                topicTitle = "History",
                topicSubTitle = "Ancient Civilizations",
                onDeleteClick = {},
                onTryAgainClick = {}
            )
        }
    }
}

@Preview(name = "Low Score (Expanded) - Dark", showBackground = true)
@Composable
private fun LowScoreExpandedDarkPreview() {
    QuizleTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            // This preview is interactive in the IDE to see the expansion
            QuizResultCardLinear(
                createdAt = System.currentTimeMillis(),
                totalQuestions = 20,
                correctAnswersCount = 5, // Low score
                topicTitle = "Mathematics",
                topicSubTitle = "Algebra I",
                onDeleteClick = {},
                onTryAgainClick = {}
            )
        }
    }
}