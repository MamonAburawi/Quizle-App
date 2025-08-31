package com.quizle.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressWithPercentage(
    percentage: Float,
    animationDuration: Int = 1000,
    size: Dp = 100.dp,
    strokeWidth: Dp = 10.dp
) {
    // Animate the percentage value for a smooth transition.
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(animationDuration)
    )

    // Determine the color based on the percentage value.
    val progressColor = when {
        animatedPercentage >= 80f -> Color.Green
        animatedPercentage > 40f -> Color.Yellow
        else -> Color.Red
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        // Use a Canvas to draw the circular progress arc.
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = animatedPercentage * 360f / 100f
            val backgroundArcColor = Color.LightGray.copy(alpha = 0.3f)
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

            // Draw the light gray background circle first.
            drawArc(
                color = backgroundArcColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )

            // Draw the progress arc on top, with the color depending on the percentage.
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = stroke
            )
        }

        // Display the percentage text in the center.
        Text(
            text = "${animatedPercentage.toInt()}%",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Preview
@Composable
private fun CircularProgressWithPercentagePreview(modifier: Modifier = Modifier) {
    CircularProgressWithPercentage(
        percentage = 60f,
        size = 200.dp,
        strokeWidth = 20.dp
    )
}