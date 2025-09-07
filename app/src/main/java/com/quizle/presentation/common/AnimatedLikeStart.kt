package com.quizle.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedLikeStar(
    modifier: Modifier = Modifier,
    initialLikedState: Boolean = false,
    likedColor: Color = Color(0xFFF9A825), // Gold color for liked state
    unlikedColor: Color = Color.LightGray, // Light gray for unliked outline
    onLikeStateChanged: (Boolean) -> Unit = {}
) {
    var isLiked by remember { mutableStateOf(initialLikedState) }

    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "Star scale animation"
    )

    val tintColor by animateColorAsState(
        targetValue = if (isLiked) likedColor else unlikedColor,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "Star color animation"
    )

    val starIcon = if (isLiked) Icons.Filled.Star else Icons.Outlined.Star

    Icon(
        imageVector = starIcon,
        contentDescription = if (isLiked) "Liked" else "Disliked",
        tint = tintColor,
        modifier = modifier
            .size(30.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale) // Apply scale directly here
            .clickable {
                isLiked = !isLiked
                onLikeStateChanged(isLiked)
            }
    )
}

@Preview
@Composable
private fun AnimatedLikeStarPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedLikeStar(
            likedColor = Color(0xFFFFA726), // Example of customizing colors
            unlikedColor = Color.Gray,
            onLikeStateChanged = { isLiked ->
                println("Star like state changed to: $isLiked")
            }
        )
    }
}