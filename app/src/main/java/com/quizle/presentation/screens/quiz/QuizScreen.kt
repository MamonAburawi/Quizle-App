package com.quizle.presentation.screens.quiz


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.quizle.domain.module.Question
import com.quizle.domain.module.Topic
import com.quizle.presentation.common.PrimaryAppBar
import com.quizle.presentation.common.PromptDialog
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.back
import com.quizle.presentation.navigation.navigateToResult
import com.quizle.presentation.theme.*
import com.quizle.presentation.util.title
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun QuizScreen(
    state: QuizState = QuizState(),
    navController: NavHostController = rememberNavController(),
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
        onNextQuestion = { onAction(QuizAction.NextQuestionButtonClick)
        },
        forceBack = {
            onAction(QuizAction.ExitQuizDialogConfirm)
        }
    )
    

    LaunchedEffect(key1 = Unit) {
        event.collect{
            when(it){
                is QuizEvent.NavigateToDashboardScreen -> {
                    navController.back()
                }
                is QuizEvent.NavigateToResultScreen ->{
                    navController.navigateToResult()
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
    onCloseClicked: () -> Unit = {},
    forceBack:() -> Unit = {},
    onTimerFinish: () -> Unit,
    onOptionClicked: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onAction: (QuizAction) -> Unit
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor)
            .statusBarsPadding(),
        topBar = {


            PrimaryAppBar(
                title = state.topic.title(),
                containerColor = PrimaryColor,
                contentColor = Color.White,
                onBack = {
                    if (state.questions.isEmpty() && state.error != null){
                        forceBack()
                    }else {
                        onCloseClicked()
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            if (state.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = Color.White
                )
            }

            if (state.error != null){
                Text(
                    text = state.error,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            }

            if (!state.isLoading && state.questions.isNotEmpty()){
                QuizElements(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = state,
                    onTimerFinish = onTimerFinish,
                    onOptionClicked = {onOptionClicked(it)},
                    onNextQuestion = onNextQuestion

                )

            }

        }




        PromptDialog(
            modifier = Modifier
                .fillMaxSize(),
            title = "Exit Quiz",
            subTitle = "Are you sure you want to exit? you won't be able to continue from where you left off.",
            isOpen = state.isQuizExitDialogOpen,
            onCancel = {
                onAction(QuizAction.ExitQuizDialogDismiss)
            },
            onPositiveBtnClicked = {
                onAction(QuizAction.ExitQuizDialogConfirm)
            },
            positiveBtnText = "Exit",
            negativeBtnText = "Cancel",
        )


        PromptDialog(
            modifier = Modifier
                .fillMaxSize(),
            title = "Time's Up!",
            subTitle = "The time limit has been reached. See your results or try again.",
            isOpen = state.isTimeUpDialogOpen,
            onPositiveBtnClicked = {
                onAction(QuizAction.TimesUpDialogSeeResultClick)
            },
            positiveBtnText = "See result",
        )



    }

}

@Composable
fun QuizElements(
    modifier: Modifier = Modifier,
    state: QuizState,
    onTimerFinish: () -> Unit,
    onOptionClicked: (Int) -> Unit,
    onNextQuestion: () -> Unit

) {
    val currentIndex = state.currentQuestionIndex
    val currentQuestion = state.questions[currentIndex]


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.padding(vertical = 15.dp))

        ProgressTracker(
            modifier = Modifier,
            currentQuestionIndex = state.currentQuestionIndex,
            questionsCount = state.questions.size,
        )

        Spacer(modifier = Modifier.padding(vertical = 15.dp))

        if (state.quizTimeEnabled){
            val topicTimeInMin = state.topic.quizTimeInMin
            val customTimeInMin = state.customTimeInMin
            val timeInMin = if (state.switchToCustomTime) customTimeInMin else topicTimeInMin
            timeInMin?.let {
                AnimatedQuizTimer(
                    modifier = Modifier,
                    timeInMin = it,
                    fontSize = 25.sp,
                    timerSize = 120,
                    onTimerFinish = onTimerFinish
                )
            }
        }




        Spacer(modifier = Modifier.height(25.dp))

        QuestionSection(
            modifier = Modifier,
            textQuestion = currentQuestion.questionText,
            options = currentQuestion.allOptions,
            correctAnswer = currentQuestion.correctAnswer,
            currentQuestionIndex = currentIndex,
            onOptionClicked = {
                onOptionClicked(it)
            },
            onNextQuestionClicked = {
                onNextQuestion()
            }
        )

    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun AnimatedQuizTimer(
    modifier: Modifier = Modifier,
    timeInMin: Int,
    timerSize: Int = 120,
    fontSize: TextUnit = 36.sp,
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
            timeLeft > totalTimeInSecond * 0.5f -> Green // Green for first half
            timeLeft > totalTimeInSecond * 0.2f -> Yellow // Yellow for the middle
            else -> Red // Red for the last part
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
private fun ProgressTracker(
    modifier: Modifier = Modifier,
    currentQuestionIndex: Int,
    questionsCount : Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val currentProgress = (currentQuestionIndex + 1).toFloat() / questionsCount.toFloat()
        LinearProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .padding(horizontal = 16.dp),
            color = Color.Green,
            trackColor = Color.LightGray
        )

        Text(
            text = "${currentQuestionIndex + 1} / ${questionsCount}",
            modifier = Modifier.padding(top = 8.dp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}
@Composable
fun QuizOptionButton(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    isCorrect: Boolean,
    isAnswered: Boolean
) {
    // Animate the button's background color
    val buttonColor by animateColorAsState(
        targetValue = when {
            // If answered and this option is correct, show green
            isAnswered && isCorrect -> Green
            // If answered and this is the selected wrong option, show red
            isAnswered && isSelected && !isCorrect -> Red
            // Otherwise, use the default surface color
            else -> colorSurface
        },
        animationSpec = tween(durationMillis = 300),
        label = "buttonColorAnimation"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
        ,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(0.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun QuestionSection(
    modifier: Modifier = Modifier,
    textQuestion: String,
    options: List<String>,
    currentQuestionIndex: Int,
    correctAnswer: String,
    onOptionClicked: (Int) -> Unit,
    onNextQuestionClicked: () -> Unit
) {
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var showNextButton by remember { mutableStateOf(false) }

    val actualQuestionNum = currentQuestionIndex + 1

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "$actualQuestionNum- $textQuestion",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            itemsIndexed(options) { index, option ->
                QuizOptionButton(
                    text = option,
                    onClick = {
                        if (selectedOptionIndex == null) {
                            selectedOptionIndex = index
                            showNextButton = true
                            onOptionClicked(index)
                        }
                    },
                    isSelected = selectedOptionIndex == index,
                    isCorrect = option == correctAnswer,
                    isAnswered = selectedOptionIndex != null
                )
            }
        }
//        Spacer(modifier = Modifier.weight(1f))
        if (showNextButton) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(0.2f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        onNextQuestionClicked()
                        // Reset state for the next question
                        selectedOptionIndex = null
                        showNextButton = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorSurface,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Next Question",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }


    }
}



@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
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

    QuizScreenContent(
        state = state,
        onRefresh = {},
        onAction = {},
        onCloseClicked = {},
        onTimerFinish = {},
        onOptionClicked = {},
        onNextQuestion = {}
    )
}