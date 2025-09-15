package com.quizle.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.quizle.R
import com.quizle.presentation.theme.QuizleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopicCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    hexColor: String,
    onCardClick: () -> Unit,
    viewsCount: Int = 0,
    disLikeCount: Int = 0,
    likeCount: Int = 0,
    timeInMin: Int? = 0,
    onFavorite: (Boolean) -> Unit = {},
    isAvailable: Boolean = true,
    isFavorite: Boolean = false,
    onContainerColor: Color = Color.White // Color for content on top of the hex color
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(hexColor.toColorInt())
        ),
        onClick = onCardClick
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top section with dynamic background
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = onContainerColor,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                // Assuming AnimatedLikeStar is designed to work on a dynamic background
                AnimatedLikeStar(
                    initialLikedState = isFavorite,
                    onLikeStateChanged = { onFavorite(!isFavorite) }
                )
            }

            // Bottom section, now theme-aware
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface) // NEW: Theme-aware background
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f, fill = false),
                        text = subtitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface, // NEW: Theme-aware text
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!isAvailable) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                // NEW: Themed "Soon" tag
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            text = "Soon",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconWithText(icon = R.drawable.ic_views, text = viewsCount.toString())
                        IconWithText(icon = R.drawable.ic_like, text = likeCount.toString())
                        IconWithText(icon = R.drawable.ic_dislike, text = disLikeCount.toString())
                    }
                    if (timeInMin != null && timeInMin > 0) {
                        IconWithText(
                            icon = R.drawable.ic_timer,
                            text = "$timeInMin Min",
                            icSize = 20.dp,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IconWithText(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    icSize: Dp = 16.dp,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = text,
            modifier = Modifier.size(icSize),
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = fontSize,
            color = contentColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// A placeholder for the AnimatedLikeStar composable for preview purposes.
@Composable
fun AnimatedLikeStar(initialLikedState: Boolean, onLikeStateChanged: (Boolean) -> Unit) {
    IconToggleButton(checked = initialLikedState, onCheckedChange = onLikeStateChanged) {
        Icon(
            painter = if (initialLikedState) painterResource(R.drawable.ic_bookmark_save) else painterResource(R.drawable.ic_bookmark_unsave),
            contentDescription = "Favorite",
            tint = Color.White
        )
    }
}




@Preview(name = "Cards - Light Theme", showBackground = true)
@Composable
private fun SecondaryTopicCardLightPreview() {
    QuizleTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SecondaryTopicCard(
                title = "Technical Support",
                subtitle = "Ideas & Suggestions",
                hexColor = "#FF5733",
                isFavorite = true,
                onCardClick = {}
            )
            SecondaryTopicCard(
                title = "New Feature Roadmap",
                subtitle = "Coming in Q4",
                hexColor = "#3375FF",
                isAvailable = false,
                onCardClick = {}
            )
        }
    }
}

@Preview(name = "Cards - Dark Theme", showBackground = true)
@Composable
private fun SecondaryTopicCardDarkPreview() {
    QuizleTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SecondaryTopicCard(
                title = "Technical Support",
                subtitle = "Ideas & Suggestions",
                hexColor = "#FF5733",
                isFavorite = true,
                onCardClick = {}
            )
            SecondaryTopicCard(
                title = "New Feature Roadmap",
                subtitle = "Coming in Q4",
                hexColor = "#3375FF",
                isAvailable = false,
                onCardClick = {}
            )
        }
    }
}