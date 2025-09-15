package com.quizle.presentation.screens.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.domain.module.Question
import com.quizle.domain.module.QuestionWithUserAnswer
import com.quizle.domain.module.Topic
import com.quizle.presentation.navigation.navigateToIssueReport
import com.quizle.domain.module.UserAnswer
import com.quizle.presentation.common.QuizResultCard
import com.quizle.presentation.navigation.navigateToDashboard
import com.quizle.presentation.theme.SurfaceColor
import com.quizle.presentation.util.subTitle
import com.quizle.presentation.util.title

@Composable
fun ResultScreen(
    navController: NavHostController,
    state: ResultState
) {

    BackHandler {  }
    ResultScreenContent(
        state = state,
        onReport = { questionId ->
            navController.navigateToIssueReport(questionId)
        },
        onReturnToHome = {
            navController.navigateToDashboard()
        }
    )

}

@Composable
fun ResultScreenContent(
    state: ResultState,
    onReport:(String) -> Unit,
    onReturnToHome: () -> Unit
) {
    val questions = state.questionsWithAnswers.map { it.question }
    val answers = state.questionsWithAnswers.map { it.selectedOption }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (state.error != null && questions.isEmpty() && !state.isLoading && answers.isEmpty()){
            Text(
                text = state.error,
                color = Color.Black,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }
        if (state.isLoading){
            CircularProgressIndicator()
        }
        if (!state.isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.9f),
                contentPadding = PaddingValues(20.dp)
            ) {
                item {
                    QuizResultCard(
                        createdAt = System.currentTimeMillis(),
                        totalQuestions = state.totalQuestionsCount,
                        correctAnswersCount = state.correctAnswersCount,
                        topicTitle = state.topic.title(),
                        topicSubTitle = state.topic.subTitle(),
                    )

                }
                item {
                    Text(
                        modifier = Modifier.padding(top = 50.dp, bottom = 10.dp),
                        text = stringResource(R.string.quiz_question),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                }
                itemsIndexed(items = questions){ index, question ->
                    val selectedOption = answers.find { it == question.correctAnswer }
                    QuestionItem(
                        question = question.questionText,
                        correctAnswer = question.correctAnswer,
                        options = question.allOptions,
                        selectedOption = selectedOption,
                        questionIndex = index,
                        explanation = question.explanation,
                        onReport = {
                            val questionId = question.id
                            onReport(questionId)
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        text = stringResource(R.string.finish),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 10.dp),
                    onClick = { onReturnToHome() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceColor
                    )
                ) {
                    Text(stringResource(R.string.return_home))
                }
            }

        }

    }


}




@Preview(showBackground = true)
@Composable
private fun ResultScreenPreview() {

    val questions = List(size = 10){ index ->
        Question(
            id = "${index + 1}" ,
            questionText = "Which famous painting by Leonardo da Vinci is known for its enigmatic smile?",
            correctAnswer = "Mona Lisa",
            allOptions = listOf("The Last Supper", "Vitruvian Man", "Salvator Mundi", "Mona Lisa"),
            explanation = "The Mona Lisa is a half-length portrait painting by Italian artist Leonardo da Vinci. '",
            topicId = "",
            masterOwnerId = ""
        )
    }
    val answers = listOf(
        UserAnswer("1", "The Last Supper",""),
        UserAnswer("2", "The Last Supper", ""),
        UserAnswer("3", "Vitruvian Man", ""),
        UserAnswer("8", "Mona Lisa", "")
    )

    val topic = Topic(
        titleEnglish = "Fine Arts",
        subtitleEnglish = "A world of creativity and beauty",
        titleArabic = "الفنون الجميلة",
        subtitleArabic = "عالم من الإبداع والجمال\"",
    )


    val state = ResultState(
        totalQuestionsCount = 10,
        correctAnswersCount = 8,
        questionsWithAnswers = listOf(
            QuestionWithUserAnswer(
                question = questions[0],
                selectedOption = answers[0].selectedOption
            ),
            QuestionWithUserAnswer(
                question = questions[1],
                selectedOption = answers[1].selectedOption
            ),
        ),
//        questions = questions,
//        answers = answers,
        topic = topic,
        isLoading = false,
        error = "this is error"
    )
    ResultScreenContent(
        state = state,
        onReport = {},
        onReturnToHome = { }
    )

}