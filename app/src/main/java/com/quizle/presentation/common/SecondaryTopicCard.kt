package com.quizle.presentation.common

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
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

@Composable
fun SecondaryTopicCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    hexColor: String,
    viewsCount: Int = 0,
    disLikeCount: Int = 0,
    likeCount: Int = 0,
    timeInMin: Int? = 0,
    onFavorite: (Boolean) -> Unit = {},
    isAvailable: Boolean = true,
    isFavorite: Boolean = false,
    onCardClick: () -> Unit
) {
    val icFavorite = if (isFavorite) R.drawable.ic_bookmark_save else R.drawable.ic_bookmark_unsave
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(hexColor.toColorInt())
        ),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                AnimatedLikeStar(
                    initialLikedState = isFavorite,
                    onLikeStateChanged = {
                        onFavorite(!isFavorite)
                    }
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        modifier = Modifier.weight(0.9f),
                        text = subtitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    if(!isAvailable){
                        Text(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .background(Color.Red)
                                .weight(0.15f)
                                .padding(horizontal = 5.dp, vertical = 2.dp),
                            text = "Soon",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                   Row(
                       modifier = Modifier,
                       horizontalArrangement = Arrangement
                           .spacedBy(15.dp),
                       verticalAlignment = Alignment.CenterVertically,
                   ) {
                       IconWithText(
                           icon = R.drawable.ic_views,
                           text = viewsCount.toString(),

                       )
                       IconWithText(
                           icon = R.drawable.ic_like,
                           text = likeCount.toString(),

                       )
                       IconWithText(
                           icon = R.drawable.ic_dislike,
                           text = disLikeCount.toString(),

                       )
                   }

                    if (timeInMin != null){
                        IconWithText(
                            icon = R.drawable.ic_timer,
                            text = "$timeInMin Min",
                            icSize = 25.dp,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
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
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    icSize: Dp = 16.dp

) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(icon) ,
            contentDescription = text,
            modifier = Modifier.size(icSize),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
        Text(
            text = text,
            fontSize = fontSize,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }

}

@Preview
@Composable
private fun SecondaryTopicCardPreview() {
    SecondaryTopicCard(
        title = "Technical Support",
        subtitle = "Ideas & Suggestions",
        hexColor = "#FF5733",
        disLikeCount = 52,
        likeCount = 41,
        viewsCount = 150,
        timeInMin = 45,
        onCardClick = {}
    )
}