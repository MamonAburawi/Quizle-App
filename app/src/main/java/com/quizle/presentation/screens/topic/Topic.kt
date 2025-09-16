package com.quizle.presentation.screens.topic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.domain.module.Topic
import com.quizle.presentation.common.*
import com.quizle.presentation.navigation.navigateToQuiz
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.util.subTitle
import com.quizle.presentation.util.title
import kotlinx.coroutines.flow.Flow

// TopicScreen logic remains the same
@Composable
fun TopicScreen(
    navController: NavHostController,
    toastManager: ToastMessageController,
    state: TopicState,
    onAction: (TopicAction)-> Unit,
    event: Flow<TopicEvent>
) {

    TopicContent(
        state = state,
        onSearchClick = {
            onAction(TopicAction.SearchButtonClicked(it))
        },
        onTopicClick = {
            onAction(TopicAction.TopicCardClicked(it))
        }
    )

    LaunchedEffect(key1 = Unit) {
        event.collect{
            when(it){
                is TopicEvent.NavigateToQuiz -> {
                    navController.navigateToQuiz(it.topicId)
                }
                is TopicEvent.ShowMessage -> {
                    toastManager.showToast(it.message, it.type)
                }
            }
        }
    }


}

@Composable
private fun TopicContent(
    state: TopicState,
    onSearchClick: (String) -> Unit,
    onTopicClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier.padding(16.dp),
                text = searchQuery,
                onTextChange = {
                    searchQuery = it
                }, // Update text in state
                hint = stringResource(R.string.search_for_title_subtitle_or_tag),
                onSearchClick = onSearchClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isLoading -> {
                    AnimatedLoadingDotsText(
                        text = stringResource(R.string.loading),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                state.topics.isNotEmpty() -> {
                    TopicList(
                        topics = state.topics,
                        onTopicClick = onTopicClick
                    )
                }
                else -> {
                    // Show a message for error or empty states
                    val message = state.error ?: stringResource(R.string.try_to_search_for_a_topic)
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
}

@Composable
private fun TopicList(
    modifier: Modifier = Modifier,
    topics: List<Topic>,
    onTopicClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = topics, key = { it.id }) { topic ->
            // SecondaryTopicCard is already theme-aware from our previous refactor
            SecondaryTopicCard(
                title = topic.title(),
                subtitle = topic.subTitle(),
                hexColor = topic.topicColor,
                viewsCount = topic.viewsCount,
                likeCount = topic.likeCount,
                disLikeCount = topic.disLikeCount,
                timeInMin = topic.quizTimeInMin,
                onCardClick = { onTopicClick(topic.id) },
            )
        }
    }
}


// --- PREVIEWS ---

@Preview(name = "Topic - Populated List (Light)", showBackground = true)
@Composable
private fun TopicPopulatedLightPreview() {
    QuizleTheme(darkTheme = false) {
        val list = List(3) {
            Topic(
                id = it.toString(),
                titleEnglish = "Technical Support $it",
                subtitleEnglish = "Ideas & Suggestions $it",
                topicColor = "#FF5733"
            )
        }
        val state = TopicState(topics = list)
        TopicContent(state, {}, {})
    }
}

@Preview(name = "Topic - Loading (Dark)", showBackground = true)
@Composable
private fun TopicLoadingDarkPreview() {
    QuizleTheme(darkTheme = true) {
        val state = TopicState(isLoading = true)
        TopicContent(state, {}, {})
    }
}

@Preview(name = "Topic - Empty (Light)", showBackground = true)
@Composable
private fun TopicEmptyLightPreview() {
    QuizleTheme(darkTheme = false) {
        val state = TopicState(topics = emptyList())
        TopicContent(state, {}, {})
    }
}

