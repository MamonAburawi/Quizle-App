package com.quizle.presentation.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.R
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopicCard(
    onTopicClick: () -> Unit,
    imageRes: Int,
    topicName: String,
    timeInMin: Int? = null,
    showQuizTime: Boolean,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.extendedColors.surfaceColor
    )
) {
    val scale = remember { Animatable(1f) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        val targetValue = if (isPressed) 0.95f else 1f
        scale.animateTo(
            targetValue = targetValue,
            animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
        )
    }

    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        shape = RoundedCornerShape(12.dp),
        colors = colors, // Use the new colors parameter
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        onClick = onTopicClick,
        interactionSource = interactionSource
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = topicName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = topicName,
                    // NEW: Text color is now theme-aware
                    color = MaterialTheme.extendedColors.onSurfaceColor,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }

            if (showQuizTime && timeInMin != null && timeInMin > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                ) {
                    // Assuming TimeDisplay is also theme-aware.
                    // For the preview, we'll use a simple Text composable.
                    TimeDisplay(timeInMinutes = timeInMin)
                }
            }
        }
    }
}



@Preview(name = "Primary Topic Card - Light", showBackground = true)
@Composable
private fun PrimaryTopicCardLightPreview() {
    QuizleTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            PrimaryTopicCard(
                onTopicClick = {},
                imageRes = R.drawable.ic_app_transparent,
                topicName = "History of Ancient Rome",
                timeInMin = 45,
                showQuizTime = true
            )
        }
    }
}

@Preview(name = "PrimaryTopic Card - Dark", showBackground = true)
@Composable
private fun PrimaryTopicCardDarkPreview() {
    QuizleTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            PrimaryTopicCard(
                onTopicClick = {},
                imageRes = R.drawable.ic_app_transparent,
                topicName = "History of Ancient Rome",
                timeInMin = 45,
                showQuizTime = true
            )
        }
    }
}
