package com.quizle.presentation.screens.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.R
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.success
import com.quizle.presentation.util.getChatFromIndex


@Composable
fun QuestionItem(
    modifier: Modifier = Modifier,
    question: String,
    correctAnswer: String,
    options: List<String>,
    selectedOption: String?,
    questionIndex: Int,
    explanation: String,
    onReport:() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(bottom = 5.dp),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.SpaceBetween
       ) {
           Text(
               modifier = Modifier
                   .weight(0.9f),
               text = "Q${questionIndex + 1}- $question",
               fontSize = MaterialTheme.typography.bodyLarge.fontSize,
               fontWeight = FontWeight.Bold,
               textAlign = TextAlign.Start,
           )
           IconButton(
               modifier = Modifier.weight(0.1f),
               onClick = {onReport()}
           ){
               Icon(
                   painter = painterResource(R.drawable.ic_info),
                   contentDescription = "Icon Report",
               )
           }
       }
        options.forEachIndexed { index, option ->
            val color = if (selectedOption != null) {
                when (option) {
                    correctAnswer -> Color.success
                    selectedOption -> MaterialTheme.colorScheme.error
                    else -> Color.DarkGray // Or any other color for non-selected, non-correct options
                }
            } else {
                Color.DarkGray
            }
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "${index.getChatFromIndex()} $option",
                color = color,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
        if (selectedOption == null){
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "This Question is not answered!",
                color = Color.Red,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
        if (selectedOption != null && selectedOption != correctAnswer){
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Explanation: $explanation",
                color = Color.Gray,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
    }



}


@Preview(showBackground = true)
@Composable
private fun QuestionItemNotAnsweredLightPreview(
) {
    QuizleTheme(darkTheme = false) {
        QuestionItem(
            question = "What is the capital of France?",
            correctAnswer = "Paris",
            options = listOf("Paris", "London", "Berlin", "Madrid"),
            selectedOption = null,
            questionIndex = 0,
            explanation = "Paris is the capital of France.",
            onReport = {}
        )
    }

}





@Preview(showBackground = true)
@Composable
private fun QuestionItemCorrectDarkPreview(
) {
    QuizleTheme(darkTheme = true) {
        QuestionItem(
            question = "What is the capital of France?",
            correctAnswer = "Paris",
            options = listOf("Paris", "London", "Berlin", "Madrid"),
            selectedOption = "Paris",
            questionIndex = 0,
            explanation = "Paris is the capital of France.",
            onReport = {}
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun QuestionItemInCorrectPreview(
) {
    QuestionItem(
        question = "What is the capital of France?",
        correctAnswer = "Paris",
        options = listOf("Paris", "London", "Berlin", "Madrid"),
        selectedOption = "Berlin",
        questionIndex = 0,
        explanation = "Paris is the capital of France.",
        onReport = {}
    )
}
