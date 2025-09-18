package com.quizle.presentation.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.domain.module.QuizResult
import com.quizle.presentation.common.AnimatedLoadingDotsText
import com.quizle.presentation.common.QuizResultCardLinear
import com.quizle.presentation.common.SearchBar
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.navigateToQuiz
import com.quizle.presentation.theme.QuizleTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun HistoryScreen(
    navController: NavHostController,
    toastManager: ToastMessageController,
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
    event: Flow<HistoryEvent>,
    appNavController: NavHostController
) {
    HistoryContent(
        state = state,
        onSearchClick = { query -> onAction(HistoryAction.SearchButtonClicked(query)) },
        onQuizTryAgain = { topicId -> onAction(HistoryAction.TryQuizButtonClicked(topicId)) },
        onDeleteResultClicked = { resultId, createdAt -> onAction(HistoryAction.DeleteButtonClicked(resultId, createdAt)) },
    )

    LaunchedEffect(key1 = Unit) {
        event.collect {
            when (it) {
                is HistoryEvent.ShowMessage -> toastManager.showToast(it.message, it.type)
                is HistoryEvent.NavigateToQuiz -> appNavController.navigateToQuiz(it.topicId)
            }
        }
    }
}

@Composable
private fun HistoryContent(
    state: HistoryState,
    onSearchClick: (String) -> Unit,
    onQuizTryAgain: (String) -> Unit,
    onDeleteResultClicked: (Int, Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier.padding(16.dp),
                onTextChange = {
                    searchQuery = it
                }, // Update text in state
                hint = stringResource(R.string.search_for_title_subtitle_or_tag),
                onSearchClick = onSearchClick,
                isLoading = state.isLoading,
                text = searchQuery
            )
        }
    ) { innerPadding ->
        // NEW: Removed hardcoded background. Scaffold provides the correct theme background.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                // Assuming AnimatedLoadingDotsText is theme-aware
                AnimatedLoadingDotsText(
                    text = stringResource(R.string.loading),
                    fontWeight = FontWeight.SemiBold
                )
            } else if (state.quizResults.isNotEmpty()) {
                QuizResultList(
                    quizResults = state.quizResults,
                    onQuizTryAgain = onQuizTryAgain,
                    onDeleteResultClicked = onDeleteResultClicked
                )
            } else {
                // Show a message for error or empty states
                val message = when {
                    state.error != null -> state.error
                    else -> stringResource(R.string.try_to_search_for_a_topic)
                }
                Text(
                    text = message,
                    fontWeight = FontWeight.SemiBold,
                    // NEW: Themed color
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun QuizResultList(
    modifier: Modifier = Modifier,
    quizResults: List<QuizResult>,
    onQuizTryAgain: (String) -> Unit,
    onDeleteResultClicked: (Int, Long) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = quizResults, key = { it.id }) { quiz ->
            QuizResultCardLinear(
                createdAt = quiz.createdAt,
                totalQuestions = quiz.totalQuestions,
                correctAnswersCount = quiz.correctAnswersCount,
                topicTitle = quiz.topicTitleEn,
                topicSubTitle = quiz.topicSubTitleEn,
                onDeleteClick = { onDeleteResultClicked(quiz.id, quiz.createdAt) },
                onTryAgainClick = { onQuizTryAgain(quiz.topicId) },
            )
        }
    }
}


@Preview(name = "History - List (Light)", showBackground = true)
@Composable
private fun HistoryLightPreview() {
    QuizleTheme(darkTheme = false) {
        val list = List(3) {
            QuizResult(
                id = it,
                createdAt = System.currentTimeMillis(),
                totalQuestions = 20,
                correctAnswersCount = 15,
                topicTitleEn = "Quiz Title $it",
                topicSubTitleEn = "Quiz Subtitle $it",
                topicId = it.toString()
            )
        }
        val state = HistoryState(quizResults = list)
        HistoryContent(state, {}, {}, { _, _ -> })
    }
}

@Preview(name = "History - Loading (Dark)", showBackground = true)
@Composable
private fun HistoryLoadingDarkPreview() {
    QuizleTheme(darkTheme = true) {
        val state = HistoryState(isLoading = true)
        HistoryContent(state, {}, {}, { _, _ -> })
    }
}

@Preview(name = "History - Empty (Light)", showBackground = true)
@Composable
private fun HistoryEmptyLightPreview() {
    QuizleTheme(darkTheme = false) {
        val state = HistoryState(quizResults = emptyList())
        HistoryContent(state, {}, {},{ _, _ -> })
    }
}



