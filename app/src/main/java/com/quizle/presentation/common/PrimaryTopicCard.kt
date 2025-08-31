package com.quizle.presentation.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizle.R
import com.quizle.presentation.theme.CardBackground
import kotlinx.coroutines.launch



@Composable
fun PrimaryTopicCard(
    onTopicClick: () -> Unit,
    imageRes: Int,
    topicName: String,
    timeInMin: Int? = null,
    showQuizTime: Boolean
//    enabled: Boolean = true,
) {
    // 1. Animation State: Create an Animatable float for scale
    val scale = remember { Animatable(1f) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val coroutineScope = rememberCoroutineScope()

    // 2. Animate scale based on press state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            coroutineScope.launch {
                scale.animateTo(
                    targetValue = 0.95f, // Scale down slightly when pressed
                    animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
                )
            }
        } else {
            coroutineScope.launch {
                scale.animateTo(
                    targetValue = 1f, // Return to original size when released
                    animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
                )
            }
        }
    }

    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            // 3. Apply the animated scale using graphicsLayer
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        // 4. Attach interactionSource to the clickable modifier
        onClick = {
            onTopicClick()
        },
        interactionSource = interactionSource
    ) {
       Box(
           modifier = Modifier
               .fillMaxSize()
       ){

           Column(
               modifier = Modifier.fillMaxSize(),
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
//               val imageUrl = "https://quizle.s3.us-east-1.amazonaws.com/ImageProfile/img_profile2.jpg"
//               AsyncImage(
//                   model = imageUrl,
//                   contentDescription = "My S3 Image",
//                   modifier = Modifier.fillMaxSize()
//               )
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
                   color = Color.White,
                   fontWeight = FontWeight.Medium,
                   fontSize = MaterialTheme.typography.bodySmall.fontSize,
                   maxLines = 3,
                   overflow = TextOverflow.Ellipsis,
                   textAlign = TextAlign.Center,
               )
           }

           if (showQuizTime){
               timeInMin?.let {
                   if (it > 0){
                       Row(
                           modifier = Modifier
                               .align(Alignment.TopStart)
                               .padding(top = 4.dp, start = 4.dp),
                           verticalAlignment = Alignment.CenterVertically
                       ) {

                           TimeDisplay(timeInMinutes = it)
                       }
                   }

               }
           }

       }
    }
}






@Preview
@Composable
private fun PrimaryTopicCardPreview() {
    PrimaryTopicCard(
        onTopicClick = {},
        imageRes = R.drawable.ic_app_transparent,
        topicName = "Quiz Party",
        timeInMin = 45,
        showQuizTime = true
    )
}