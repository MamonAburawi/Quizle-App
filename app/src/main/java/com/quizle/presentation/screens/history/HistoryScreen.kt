package com.quizle.presentation.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.domain.module.QuizResult
import com.quizle.presentation.common.AnimatedLoadingDotsText
import com.quizle.presentation.common.QuizResultCardLinear
import com.quizle.presentation.common.SearchBar
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.navigateToQuiz
import com.quizle.presentation.theme.DarkBackground
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
        onSearchClick = {
            onAction(HistoryAction.SearchButtonClicked(it))
        },
        onQuizTryAgain = { topicId ->
            onAction(HistoryAction.TryQuizButtonClicked(topicId))
        },
        onDeleteResultClicked = { resultId, createdAt ->
            onAction(HistoryAction.DeleteButtonClicked(resultId,createdAt))
        }
    )


    LaunchedEffect(key1 = Unit) {
        event.collect{
            when(it){
                is HistoryEvent.ShowMessage -> {
                    toastManager.showToast(it.message, it.type)
                }

                is HistoryEvent.NavigateToQuiz -> {
                    appNavController.navigateToQuiz(it.topicId)
                }
            }
        }
    }


}

@Composable
private fun HistoryContent(
    state: HistoryState,
    onSearchClick:(String)-> Unit,
    onQuizTryAgain: (String) -> Unit,
    onDeleteResultClicked: (Int, Long) -> Unit
) {
    Scaffold(
        modifier =  Modifier
            .fillMaxSize(),
        topBar = {
            SearchBar(
                modifier = Modifier.padding(25.dp),
                hint = stringResource(R.string.search_for_title_subtitle_or_tag),
                onTextChange = {  },
                onSearchClick = { query ->
                    onSearchClick(query)
                }
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (state.isLoading) {
                AnimatedLoadingDotsText(
                    text = stringResource(R.string.loading),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (!state.isLoading && state.quizResults.isNotEmpty()) {
                QuizResultList(
                    quizResults = state.quizResults,
                    onQuizTryAgain = {
                        onQuizTryAgain(it)
                    },
                    onDeleteResultClicked = { quizId, createdAt ->
                        onDeleteResultClicked(quizId,createdAt)
                    }
                )
            }



            val message = if (state.error != null && state.initMessage.isEmpty()) {
                state.error
            } else if (state.quizResults.isEmpty() && !state.isLoading) {
                state.initMessage
            } else {
                ""
            }



            Text(
                text = message,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )



//            if(state.error != null){
//                Text(
//                    text = state.error,
//                    fontWeight = FontWeight.SemiBold,
//                    color = Color.White,
//                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
//                )
//            }




        }
    }
}


@Composable
private fun QuizResultList(
    modifier: Modifier = Modifier,
    quizResults: List<QuizResult>,
    onQuizTryAgain:(String)-> Unit,
    onDeleteResultClicked:(Int, Long) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 10.dp)

    ) {
        items(items = quizResults){ quiz ->
            QuizResultCardLinear(
                createdAt = quiz.createdAt,
                totalQuestions = quiz.totalQuestions,
                correctAnswersCount = quiz.correctAnswersCount,
                topicTitle = quiz.topicTitleEn,
                topicSubTitle = quiz.topicSubTitleEn,
                onDeleteClick = {
                    onDeleteResultClicked(quiz.id, quiz.createdAt)
                },
                onTryAgainClick = {
                    onQuizTryAgain(quiz.topicId)
                },
            )
        }


    }

}






@Preview(showBackground = true)
@Composable
private fun TopicPreview() {

    val list = List(20){
        QuizResult(
            createdAt = 2452556585475,
            totalQuestions = 20,
            correctAnswersCount = 15,
            topicTitleEn = "Quiz Title $it",
            topicSubTitleEn = "Quiz Subtitle $it",
            topicId = it.toString()
        )
    }

    val state = HistoryState(
        error = "",
        quizResults = emptyList(),
        initMessage = "Try to search for a topic",
        isLoading = false
    )

    HistoryContent(
        state = state,
        onSearchClick = {},
        onQuizTryAgain = {},
        onDeleteResultClicked = {s,l ->},
    )
}