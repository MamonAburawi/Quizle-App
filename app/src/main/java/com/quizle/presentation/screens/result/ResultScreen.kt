package com.quizle.presentation.screens.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
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
import com.quizle.presentation.common.LoadingScreen
import com.quizle.presentation.common.QuizResultCard
import com.quizle.presentation.navigation.navigateToDashboard
import com.quizle.presentation.navigation.navigateToIssueReport
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors
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
    onReport: (String) -> Unit,
    onReturnToHome: () -> Unit
) {
    // NEW: No hardcoded background, Scaffold provides it
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.extendedColors.backgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> LoadingScreen(isLoading = true)
                state.error != null -> Text(
                    text = state.error,
                    color = MaterialTheme.extendedColors.error, // NEW: Themed color
                    style = MaterialTheme.typography.titleMedium
                )
                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp)
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
                                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
                                text = stringResource(R.string.quiz_question),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                        itemsIndexed(items = state.questionsWithAnswers, key = { _, item -> item.question.id }) { index, item ->
                            QuestionItem(
                                question = item.question,
                                selectedOption = item.selectedOption,
                                questionIndex = index,
                                onReport = { onReport(item.question.id) },
                                explanation = item.question.explanation
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        }
                    }
                    // NEW: Themed button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = onReturnToHome,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.extendedColors.primaryColor)
                    ) {
                        Text(stringResource(R.string.return_home))
                    }
                }
            }
        }
    }
}


@Composable
fun QuestionItem(
    question: Question,
    selectedOption: String?,
    questionIndex: Int,
    explanation: String,
    onReport: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "${questionIndex + 1}. ${question.questionText}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        question.allOptions.forEach { option ->
            val isThisTheSelectedOption = option == selectedOption
            val isThisTheCorrectOption = option == question.correctAnswer

            val textColor = when {
                isThisTheCorrectOption -> Color(0xFF1B5E20) // Dark Green for Correct
                isThisTheSelectedOption && !isThisTheCorrectOption -> MaterialTheme.extendedColors.error
                else -> MaterialTheme.extendedColors.onSurfaceColor
            }
            val icon = when {
                isThisTheCorrectOption -> Icons.Default.CheckCircle
                isThisTheSelectedOption && !isThisTheCorrectOption -> Icons.Default.Close
                else -> null
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null, tint = textColor, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = option, color = textColor.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.explanation, explanation),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.extendedColors.onSurfaceColor
        )
        TextButton(onClick = onReport, modifier = Modifier.align(Alignment.End)) {
            Text(
                text = stringResource(R.string.report_an_issue),
                color = MaterialTheme.extendedColors.onSurfaceColor
            )
        }
    }
}



@Preview(name = "Result Screen - Light Theme", showBackground = true)
@Composable
private fun ResultScreenLightPreview() {
    QuizleTheme(darkTheme = false) {
        val questions = List(size = 5){ index ->
            Question(
                id = "${index + 1}" ,
                questionText = "Which famous painting by Leonardo da Vinci is known for its enigmatic smile?",
                correctAnswer = "Mona Lisa",
                allOptions = listOf("The Last Supper", "Vitru Man","Salvator Mundi","Mona Lisa"),
                explanation = "The Mona Lisa is a half-length portrait painting by Italian artist Leonardo da Vinci. Considered an archetypal masterpiece of the Italian Renaissance, it has been described as 'the best known, the most visited, the most written about, the most sung about, the most parodied work of art in the world.'",
                topicId = "101",
                masterOwnerId = "101"
            )
        }
        val state = ResultState(
            totalQuestionsCount = 10,
            correctAnswersCount = 8,
            questionsWithAnswers = listOf(
                QuestionWithUserAnswer(
                    question = questions[1]
                    , selectedOption = "Paris"
                ),
                QuestionWithUserAnswer(
                    question = questions[1],
                    selectedOption = "Earth"
                )
            ),
            topic = Topic(titleEnglish = "General Knowledge", subtitleEnglish = "A mix of topics")
        )
        ResultScreenContent(state, {}, {})
    }
}

@Preview(name = "Result Screen - Dark Theme", showBackground = true)
@Composable
private fun ResultScreenDarkPreview() {
    QuizleTheme(darkTheme = true) {
        val questions = List(size = 5){ index ->
            Question(
                id = "${index + 1}" ,
                questionText = "Which famous painting by Leonardo da Vinci is known for its enigmatic smile?",
                correctAnswer = "Mona Lisa",
                allOptions = listOf("The Last Supper", "Vitru Man","Salvator Mundi","Mona Lisa"),
                explanation = "The Mona Lisa is a half-length portrait painting by Italian artist Leonardo da Vinci. Considered an archetypal masterpiece of the Italian Renaissance, it has been described as 'the best known, the most visited, the most written about, the most sung about, the most parodied work of art in the world.'",
                topicId = "101",
                masterOwnerId = "101"
            )
        }
        val state = ResultState(
            totalQuestionsCount = 10,
            correctAnswersCount = 8,
            questionsWithAnswers = listOf(
                QuestionWithUserAnswer(
                    question = questions[1]
                    , selectedOption = "Paris"
                ),
                QuestionWithUserAnswer(
                    question = questions[1],
                    selectedOption = "Earth"
                )
            ),
            topic = Topic(titleEnglish = "General Knowledge", subtitleEnglish = "A mix of topics")
        )
        ResultScreenContent(state, {}, {})
    }
}
