package com.quizle.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.quizle.R
import com.quizle.presentation.theme.DarkBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun QuizResultCardLinear(
    createdAt: Long,
    totalQuestions: Int,
    correctAnswersCount: Int,
    topicTitle: String,
    topicSubTitle: String,
    onDeleteClick: () -> Unit,
    onTryAgainClick: () -> Unit
) {
    // State to track if the card is expanded
    var isExpanded by remember { mutableStateOf(false) }

    val progress = correctAnswersCount.toFloat() / totalQuestions.toFloat()
    val progressColor = when {
        progress >= 0.8 -> Color(0xFF4CAF50) // Green
        progress >= 0.5 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy       h:mm:a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(createdAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            isExpanded = !isExpanded
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title and Score Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = topicTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = topicSubTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

            Spacer(modifier = Modifier.height(12.dp))

            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Creation Time
            Text(
                text = stringResource(id = R.string.completion_date_message, formattedDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Animated visibility for the buttons row
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)),
                exit = shrinkVertically(animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
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
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Quiz",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.delete),
                            color = Color.Red,
                        )
                    }

                    Button(
                        onClick = onTryAgainClick,
                        colors = ButtonDefaults.buttonColors(
                            DarkBackground
                        ),
                        modifier = Modifier.weight(1f)
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

@Preview
@Composable
fun QuizResultCardLinearPreview(modifier: Modifier = Modifier) {
    QuizResultCardLinear(
        createdAt = 162,
        totalQuestions = 20,
        correctAnswersCount = 15,
        topicTitle = "Quiz Title",
        topicSubTitle = "Quiz Subtitle",
        onDeleteClick = {},
        onTryAgainClick = {}
    )
}


