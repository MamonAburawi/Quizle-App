package com.quizle.presentation.screens.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.domain.module.QuizTopic
import com.quizle.presentation.common.AnimatedLoadingDotsText
import com.quizle.presentation.common.SearchBar
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.common.SecondaryTopicCard
import com.quizle.presentation.common.rememberToastMessageController
import com.quizle.presentation.navigation.navigateToQuiz
import com.quizle.presentation.theme.DarkBackground
import com.quizle.presentation.util.subTitle
import com.quizle.presentation.util.title
import kotlinx.coroutines.flow.Flow

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
        toastManager = toastManager,
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
    toastManager: ToastMessageController,
    onSearchClick:(String)-> Unit,
    onTopicClick: (String) -> Unit
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

            if (!state.isLoading && state.topics.isNotEmpty()) {
                TopicList(
                    topics = state.topics,
                    onTopicClick = {
                        onTopicClick(it)
                    }
                )
            }


            val message = if (state.error != null && state.initMessage.isEmpty()) {
                state.error
            } else if (state.topics.isEmpty() && !state.isLoading) {
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







        }
    }
}

//object LocalizationHelper {
//
//    fun getLocalizedString(
//        localizations: Map<String, String>,
//        fallbackLanguageCode: String = "en"
//    ): String {
//        // Get the current device language code
//        val currentLanguage = Locale.getDefault().language
//
//        // 1. Try to get the title for the current language
//        return localizations[currentLanguage]
//        // 2. If not found, fall back to the specified default language (e.g., "en")
//            ?: localizations[fallbackLanguageCode]
//            // 3. If the fallback isn't found, return an empty string or a placeholder
//            ?: ""
//    }
//}


@Composable
private fun TopicList(
    modifier: Modifier = Modifier,
//    appPrefer: AppPreferences = koinInject<AppPreferences>(),
    topics: List<QuizTopic>,
    onTopicClick:(String)-> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp)

    ) {
//        val language = appPrefer.loadSettings().language
        items(items = topics){ topic ->
            SecondaryTopicCard(
                title = topic.title(),
                subtitle = topic.subTitle(),
                hexColor = topic.topicColor,
                viewsCount = topic.viewsCount,
                likeCount = topic.likeCount,
                disLikeCount = topic.disLikeCount,
                timeInMin = topic.quizTimeInMin,
                onCardClick = {
                    onTopicClick(topic.id)
                },
            )
        }
    }

}
@Preview(showBackground = true)
@Composable
private fun TopicPreview() {

    val list = List(20){
        QuizTopic(
            titleEnglish = "Technical Support $it",
            subtitleEnglish = "Ideas & Suggestions $it",
            topicColor = "#FF5733",
            viewsCount = 150,
            likeCount = 41,
            disLikeCount = 52,
            quizTimeInMin = 45
        )
    }

    val state = TopicState(
        topics = emptyList(),
        isLoading = false,
        error = null,
        initMessage = stringResource(R.string.try_to_search_for_a_topic)
    )

    TopicContent(
        state = state,
        toastManager = rememberToastMessageController(),
        onSearchClick = {},
        onTopicClick = {}
    )
}