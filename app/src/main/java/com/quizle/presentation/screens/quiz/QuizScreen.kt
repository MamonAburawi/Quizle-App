package com.quizle.presentation.screens.quiz

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.domain.module.Question
import com.quizle.domain.module.Topic
import com.quizle.domain.module.UserAnswer
import com.quizle.presentation.common.ConfirmationDialog
import com.quizle.presentation.common.LoadingScreen
import com.quizle.presentation.common.PrimaryAppBar
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.back
import com.quizle.presentation.navigation.navigateToResult
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.error
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.theme.success
import com.quizle.presentation.theme.warning
import com.quizle.presentation.util.title
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlin.Boolean


@Composable
fun QuizScreen(
    state: QuizState = QuizState(),
    navController: NavHostController,
    onAction: (QuizAction) -> Unit = {},
    event: Flow<QuizEvent> = emptyFlow(),
    onRefresh: () -> Unit = {},
    toastManager: ToastMessageController
) {

    BackHandler {}

    QuizScreenContent(
        state = state,
        onAction = onAction,
        onRefresh = onRefresh,
        onCloseClicked = { onAction(QuizAction.ExitQuizButtonClick) },
        onTimerFinish = { onAction(QuizAction.OnTimerFinish) },
        onOptionClicked = { onAction(QuizAction.SelectOption(it)) },
        onNextQuestion = { onAction(QuizAction.NextQuestionButtonClick) }
    )


    LaunchedEffect(key1 = Unit) {
        event.collect{
            when(it){
                is QuizEvent.NavigateToDashboardScreen -> {
                    navController.back()
                }
                is QuizEvent.NavigateToResultScreen ->{
                    val topicId = it.topicId
                    navController.navigateToResult(topicId)
                }
                is QuizEvent.NavigateToTopicScreen -> {
                    navController.back()
                }
                is QuizEvent.ShowToast -> {
                    val type = it.type
                    val message = it.message
                    toastManager.showToast(message, type)
                }
            }
        }
    }


    LaunchedEffect(key1 = state.currentQuestionIndex) {
        if (!state.isTimeUpDialogOpen && state.currentQuestionIndex == state.questions.lastIndex){
            onAction(QuizAction.CompleteBeforeTimesUp)
        }
    }



}
@Composable
private fun QuizScreenContent(
    state: QuizState,
    onRefresh: () -> Unit,
    onCloseClicked: () -> Unit,
    onTimerFinish: () -> Unit,
    onOptionClicked: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onAction: (QuizAction) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            PrimaryAppBar(
                title = state.topic.title(),
                onBack = onCloseClicked
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                // NEW: No background needed, it uses its own theme-aware default
                LoadingScreen(isLoading = true, initMessage = stringResource(R.string.hang_tight_your_quiz_is_loading))
            } else if (state.error != null) {
                Text(
                    text = state.error,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.extendedColors.error // NEW: Themed error color
                )
            } else if (state.questions.isNotEmpty()) {
                QuizElements(
                    state = state,
                    onTimerFinish = onTimerFinish,
                    onOptionClicked = onOptionClicked,
                    onNextQuestion = onNextQuestion
                )
            }
        }

        // ConfirmationDialogs are already theme-aware from our previous refactor
        ConfirmationDialog(
            title = stringResource(R.string.exit_quiz),
            subTitle = stringResource(R.string.exist_dialog_info),
            isOpen = state.isQuizExitDialogOpen,
            onConfirm = { onAction(QuizAction.ExitQuizDialogConfirm) },
            onDismissRequest = { onAction(QuizAction.ExitQuizDialogDismiss) },
            positiveBtnText = stringResource(R.string.exit),
            negativeBtnText = stringResource(R.string.cancel),
        )
        ConfirmationDialog(
            onDismissRequest = {},
            title = stringResource(R.string.time_s_up),
            subTitle = stringResource(R.string.time_s_up_info),
            isOpen = state.isTimeUpDialogOpen,
            onConfirm = { onAction(QuizAction.TimesUpDialogSeeResultClick) },
            positiveBtnText = stringResource(R.string.see_result),
        )
    }
}

@Composable
private fun QuizElements(
    modifier: Modifier = Modifier,
    state: QuizState,
    onTimerFinish: () -> Unit,
    onOptionClicked: (Int) -> Unit,
    onNextQuestion: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        ProgressTracker(
            currentQuestionIndex = state.currentQuestionIndex,
            questionsCount = state.questions.size,
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (state.quizTimeEnabled) {
            val timeInMin = if (state.switchToCustomTime) state.customTimeInMin else state.topic.quizTimeInMin
            timeInMin?.let {
                AnimatedQuizTimer(
                    timeInMin = it,
                    onTimerFinish = onTimerFinish
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        QuestionSection(
            currentQuestion = state.questions[state.currentQuestionIndex],
            currentQuestionIndex = state.currentQuestionIndex,
            onOptionClicked = onOptionClicked,
            onNextQuestionClicked = onNextQuestion
        )
    }
}


@SuppressLint("DefaultLocale")
@Composable
private fun AnimatedQuizTimer(
    modifier: Modifier = Modifier,
    timeInMin: Int,
    timerSize: Int = 130,
    fontSize: TextUnit = 24.sp,
    onTimerFinish: () -> Unit
) {
// Total time in seconds
    val totalTimeInSecond = timeInMin * 60f

// State for timer animation (from totalTime to 0)
    var timeLeft by remember { mutableFloatStateOf(totalTimeInSecond) }

// Calculate hours, minutes, and seconds from timeLeft
    val hours = (timeLeft.toInt() / 3600)
    val minutes = (timeLeft.toInt() % 3600 / 60)
    val seconds = (timeLeft.toInt() % 60)

// Format the time string to include hours
    val formattedTime = if (hours > 0) {
        String.format("%2d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }

    // Start a coroutine to decrement the timer
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000) // Delay for 1 second
            timeLeft -= 1
        }
        // When the timer finishes, call the provided callback
        onTimerFinish()
    }

    // Animate the sweep angle of the arc
    val animatedSweepAngle by animateFloatAsState(
        targetValue = (timeLeft / totalTimeInSecond) * 360f, // From 360 degrees down to 0
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "sweepAngleAnimation"
    )

    // Animate the color of the arc based on the time left
    val timerColor by animateColorAsState(
        targetValue = when {
            timeLeft > totalTimeInSecond * 0.5f -> Color.success // Green for first half
            timeLeft > totalTimeInSecond * 0.2f -> Color.warning // Yellow for the middle
            else -> Color.error // Red for the last part
        },
        animationSpec = tween(durationMillis = 1000),
        label = "timerColorAnimation"
    )

    // Timer UI
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(timerSize.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background circle (the full white outline)
            drawArc(
                color = Color.White.copy(alpha = 0.2f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round),
                topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                size = Size(size.width - 8.dp.toPx(), size.height - 8.dp.toPx())
            )
            // Animated foreground arc
            drawArc(
                color = timerColor,
                startAngle = -90f, // Start from the top
                sweepAngle = animatedSweepAngle,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round),
                topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                size = Size(size.width - 8.dp.toPx(), size.height - 8.dp.toPx())
            )
        }

        Text(
            text = formattedTime,
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProgressTracker(currentQuestionIndex: Int, questionsCount: Int) {
    val progress = (currentQuestionIndex + 1).toFloat() / questionsCount.toFloat()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(horizontal = 16.dp),
            color = MaterialTheme.extendedColors.primaryColor,
            trackColor = MaterialTheme.extendedColors.textPrimaryColor.copy(alpha = 0.8f)
        )
        Text(
            text = "${currentQuestionIndex + 1} / $questionsCount",
            modifier = Modifier.padding(top = 8.dp),
            // NEW: Themed color
            color = MaterialTheme.extendedColors.onBackgroundColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun QuizOptionButton(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    isCorrect: Boolean,
    isAnswered: Boolean,
    correctContainerColor: Color = Color(0xFFDCEFD6), // A light green
    onCorrectContainerColor: Color = Color(0xFF1B5E20), // A dark green
) {
    // Determine container and content colors based on state
    val containerColor by animateColorAsState(
        targetValue = when {
            isAnswered && isCorrect -> correctContainerColor
            isAnswered && isSelected && !isCorrect -> MaterialTheme.extendedColors.error
            else -> MaterialTheme.extendedColors.surfaceColor
        },
        label = ""
    )
    val contentColor by animateColorAsState(
        targetValue = when {
            isAnswered && isCorrect -> onCorrectContainerColor
            isAnswered && isSelected && !isCorrect -> MaterialTheme.extendedColors.onBackgroundColor
            else -> MaterialTheme.extendedColors.onSurfaceColor
        },
        label = ""
    )

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        ),
        enabled = !isAnswered // Disable button after an answer is given
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun QuestionSection(
    currentQuestion: Question,
    currentQuestionIndex: Int,
    onOptionClicked: (Int) -> Unit,
    onNextQuestionClicked: () -> Unit
) {
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    // Reset selection when question changes
    LaunchedEffect(currentQuestion) { selectedOptionIndex = null }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "${currentQuestionIndex + 1}. ${currentQuestion.questionText}",
                // NEW: Themed color
                color = MaterialTheme.extendedColors.onBackgroundColor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
            )
        }
        itemsIndexed(currentQuestion.allOptions) { index, option ->
            QuizOptionButton(
                text = option,
                onClick = {
                    if (selectedOptionIndex == null) {
                        selectedOptionIndex = index
                        onOptionClicked(index)
                    }
                },
                isSelected = selectedOptionIndex == index,
                isCorrect = option == currentQuestion.correctAnswer,
                isAnswered = selectedOptionIndex != null
            )
        }
        if (selectedOptionIndex != null) {
            item {
                // NEW: Themed next button
                Button(
                    onClick = onNextQuestionClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Next Question",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}



@Preview(name = "Quiz Screen - Light Theme", showBackground = true)
@Composable
private fun QuizScreenLightPreview() {
    QuizleTheme(darkTheme = false) {
        val questions = List(size = 40){ index ->
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
        val topic = Topic(
            id = "101",
            titleEnglish = "Technical Support",
            subtitleEnglish = "Ideas & Suggestions",
            topicColor = "#FF5733",
            quizTimeInMin = 120
        )
        val state = QuizState(
            questions = questions,
            currentQuestionIndex = 15,
            topic = topic,
            answers = emptyList(),
            isLoading = false,
            error = null,
            customTimeInMin = 10,
            switchToCustomTime = false,
            quizTimeEnabled = true
        )
        QuizScreenContent(state, {}, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Quiz Screen - Dark Theme", showBackground = true)
@Composable
private fun QuizScreenDarkPreview() {
    QuizleTheme(darkTheme = true) {
        val questions = List(size = 40){ index ->
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
        val topic = Topic(
            id = "101",
            titleEnglish = "Technical Support",
            subtitleEnglish = "Ideas & Suggestions",
            topicColor = "#FF5733",
            quizTimeInMin = 120
        )
        val state = QuizState(
            questions = questions,
            currentQuestionIndex = 15,
            topic = topic,
            answers = emptyList(),
            isLoading = false,
            error = null,
            customTimeInMin = 10,
            switchToCustomTime = false,
            quizTimeEnabled = true
        )
        QuizScreenContent(state, {}, {}, {}, {}, {}, {})
    }
}

