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
import com.quizle.presentation.theme.QuizleTheme

@Composable
fun CircularProgressWithPercentage(
    percentage: Float,
    animationDuration: Int = 1000,
    size: Dp = 100.dp,
    strokeWidth: Dp = 10.dp,
    // NEW: Colors are now parameters with theme-based defaults
    successColor: Color = MaterialTheme.colorScheme.primary,
    warningColor: Color = MaterialTheme.colorScheme.secondary,
    errorColor: Color = MaterialTheme.colorScheme.error,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(animationDuration),
        label = "percentageAnimation"
    )

    // The logic now uses the color parameters.
    val progressColor = when {
        animatedPercentage >= 80f -> successColor
        animatedPercentage > 40f -> warningColor
        else -> errorColor
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = animatedPercentage * 360f / 100f
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

            // Draw the background track using the new trackColor.
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )

            // Draw the progress arc.
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = stroke
            )
        }

        Text(
            text = "${animatedPercentage.toInt()}%",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            // Changed to onSurface for better semantic correctness
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CircularProgressWithPercentagePreview() {
    // The preview uses the default theme colors without any changes.
    QuizleTheme {
        CircularProgressWithPercentage(
            percentage = 100f, // This will now use the theme's 'secondary' color
            size = 200.dp,
            strokeWidth = 20.dp
        )
    }
}