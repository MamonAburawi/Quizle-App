package com.quizle.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.util.getChatFromIndex


@Composable
fun QuestionCard(
    modifier: Modifier = Modifier,
    question: String,
    options: List<String>,
    explanation: String,
    colors: CardColors = CardDefaults.cardColors() // Defaults to theme surface color
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = colors
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val rotationDegree by animateFloatAsState(
                    targetValue = if (expanded) 180f else 0f,
                    label = "arrow rotation",
                    animationSpec = tween(durationMillis = 250)
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        modifier = Modifier.rotate(rotationDegree),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp, end = 16.dp, top = 8.dp)
                        .fillMaxWidth()
                ) {
                    options.forEachIndexed { index, option ->
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "${index.getChatFromIndex()} $option",
                            // NEW: Using a theme-aware color
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "Explanation: $explanation",
                        // NEW: Using a theme-aware color with less emphasis
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


// --- PREVIEWS ---

@Preview(name = "Question Card - Light Theme")
@Composable
private fun QuestionCardLightPreview() {
    QuizleTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            QuestionCard(
                question = "What is the largest planet in our solar system?",
                options = listOf("Earth", "Mars", "Jupiter", "Saturn"),
                explanation = "Jupiter is the largest planet, more than twice as massive as all the other planets combined.",
            )
        }
    }
}

@Preview(name = "Question Card - Dark Theme")
@Composable
private fun QuestionCardDarkPreview() {
    QuizleTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            QuestionCard(
                question = "What is the largest planet in our solar system?",
                options = listOf("Earth", "Mars", "Jupiter", "Saturn"),
                explanation = "Jupiter is the largest planet, more than twice as massive as all the other planets combined.",
            )
        }
    }
}