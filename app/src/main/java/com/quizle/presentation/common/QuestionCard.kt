package com.quizle.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.util.getChatFromIndex

@Composable
fun QuestionCard(
    modifier: Modifier = Modifier,
    question: String,
    options: List<String>,
    explanation: String
) {
    var expanded by remember { mutableStateOf(true) }
    Card(
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)

        ) {
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 15.dp),
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.SpaceBetween
               ) {

                   val rotationDegree by animateFloatAsState(
                       targetValue = if (expanded) 180f else 0f, label = "arrow rotation",
                       animationSpec = tween(durationMillis = 250)
                   )

                   Text(
                       modifier = Modifier
                           .weight(0.9f),
                       text = question,
                       fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                       fontWeight = FontWeight.Bold,
                       textAlign = TextAlign.Start,
                   )
                   IconButton(
                       modifier = Modifier.weight(0.1f),
                       onClick = {
                           expanded = !expanded
                       }
                   ){
                       Icon(
                           modifier = Modifier
                               .rotate(rotationDegree),
                           imageVector = Icons.Default.ArrowDropDown,
                           contentDescription = "Icon Report",
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
                        .padding(start = 15.dp, bottom = 15.dp, end = 15.dp)
                        .fillMaxWidth()
                ) {
                    options.forEachIndexed { index, option ->
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = "${index.getChatFromIndex()} $option",
                            color = Color.DarkGray,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "Explanation: $explanation",
                        color = Color.Gray,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                }
            }


        }
    }

    

}


@Preview
@Composable
fun QuestionCardPreview(modifier: Modifier = Modifier) {
    QuestionCard(
        question = "What is the capital of France?",
        options = listOf("Paris", "London", "Berlin", "Madrid"),
        explanation = "Paris is the capital of France.",
    )
}