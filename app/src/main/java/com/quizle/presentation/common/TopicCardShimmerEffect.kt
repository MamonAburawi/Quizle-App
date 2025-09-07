package com.quizle.presentation.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun TopicCardShimmerEffect(
    modifier: Modifier = Modifier,
    // You can customize the shimmer colors
    shimmerColorShades: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.9f),
    )
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)

    ) {
        val transition = rememberInfiniteTransition(label = "Shimmer Transition")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f, // Adjust this value based on your content width/height
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1200, // Shimmer speed
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse // Or RepeatMode.Restart for a continuous left-to-right
            ),
            label = "Shimmer Translate Animation"
        )

        val brush = Brush.linearGradient(
            colors = shimmerColorShades,
            start = Offset(x = translateAnimation.value - 200, y = translateAnimation.value - 200),
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )

        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerEffectPreview() {
    TopicCardShimmerEffect(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
    )
}

