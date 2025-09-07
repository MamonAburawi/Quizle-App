@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("KotlinConstantConditions")

package com.quizle.presentation.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.quizle.R
import com.quizle.domain.module.AppRelease
import com.quizle.domain.module.Topic
import com.quizle.domain.module.User
import com.quizle.presentation.common.AnimatedLoadingDotsText
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.common.PrimaryTopicCard
import com.quizle.presentation.common.TopicCardShimmerEffect
import com.quizle.presentation.navigation.FilterParam.FILTER_TYPE_POPULAR
import com.quizle.presentation.navigation.FilterParam.FILTER_TYPE_TOP_VIEWED
import com.quizle.presentation.navigation.navigateToNotifications
import com.quizle.presentation.navigation.navigateToProfile
import com.quizle.presentation.navigation.navigateToQuiz
import com.quizle.presentation.theme.CardBackground
import com.quizle.presentation.theme.DarkBackground
import com.quizle.presentation.theme.GrayText
import com.quizle.presentation.theme.GreenAccent
import com.quizle.presentation.theme.LightPurple
import com.quizle.presentation.util.Gender
import com.quizle.presentation.util.openUrlInBrowser
import com.quizle.presentation.util.title
import kotlinx.coroutines.flow.Flow


@Composable
fun HomeScreen(
    state: HomeState,
    appNavController: NavHostController,
    onAction: (HomeAction) -> Unit,
    event: Flow<HomeEvent>,
    toastManager: ToastMessageController
) {
    val context = LocalContext.current

    HomeContent(
        state = state,
        onProfileClick = {
            appNavController.navigateToProfile()
        },
        onTopicClick = { onAction(HomeAction.TopicItemCardClicked(it))},
        onResumeQuizClick = { onAction(HomeAction.ResumeButtonClicked)},
        onTopViewedQuizzesClick = { onAction(HomeAction.MoreTopViewedQuizzesButtonClicked(key = FILTER_TYPE_TOP_VIEWED))} ,
        onPopularQuizzesClick = { onAction(HomeAction.MorePopularQuizzesButtonClicked(key = FILTER_TYPE_POPULAR)) },
        onRefresh = { onAction(HomeAction.OnRefresh) },
        onNotificationClick = {
            onAction(HomeAction.NotificationButtonClicked)
        },
        onUpdateClicked = {
          onAction(HomeAction.UpdateButtonClicked)
        }
    )


    LaunchedEffect(key1 = Unit) {
        event.collect{
            when(it){
                is HomeEvent.NavigateToProfileScreen -> {
                    appNavController.navigateToProfile()
                }
                is HomeEvent.NavigateToQuizScreen -> {
                    val topicId = it.topicId
                    appNavController.navigateToQuiz(topicId = topicId)
                }
                is HomeEvent.NavigateToTopicScreen -> {
                    val key = it.key

                   // todo navigate to topic screen
                }
                is HomeEvent.ShowToast -> {
                    val type = it.type
                    val message = it.message
                    toastManager.showToast(message, type)

                }

                is HomeEvent.NavigateToNotificationScreen -> {
                    appNavController.navigateToNotifications()
                }

                is HomeEvent.NavigateToUrlBrowser -> {
                    val newVersionUrl  = it.url
                    context.openUrlInBrowser(newVersionUrl)
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    state: HomeState,
    onProfileClick: () -> Unit,
    onPopularQuizzesClick: () -> Unit,
    onTopViewedQuizzesClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    onResumeQuizClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onRefresh: () -> Unit,
    onUpdateClicked: () -> Unit
) {

    val user = state.user
    val gender = user.gender
    val icAvatar = if (gender == Gender.Male.name) R.drawable.ic_male_avatar else R.drawable.ic_female_avatar
    val isTopicLoading = state.isTopicLoading
    val showQuizTime = user.settings.enableQuizTime
    val customTimeInMin = user.settings.customQuizTimeInMin



    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        onRefresh = {
            onRefresh()
        }
    ){
        val isArabic = state.user.language == "ar"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp)

            ) {
                item(
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    ProfileSection(
                        icAvatar = icAvatar,
                        name = state.user.userName,
                        imageProfile = state.user.imageProfile,
                        onProfileClick = onProfileClick,
                        onNotificationClick = onNotificationClick
                    )
                }

                item(
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    StatisticsOverview(
                        modifier = Modifier.padding(vertical = 20.dp),
                        totalQuizzes = state.totalQuizzies,
                        correctAnswers = state.correctAnswers,
                        accurateRate = state.accurateRate
                    )
                }


                if (state.isNewAppUpdatesAvailable){
                    item(
                        span = {
                            GridItemSpan(maxLineSpan)
                        }
                    ) {
                        NewVersionUpdateCard(
                            versionName = state.appRelease.versionName,
                            releaseNotes =  state.appRelease.releaseNoteEn,
                            onUpdateButtonClicked = onUpdateClicked
                        )
                    }
                }


                if (isTopicLoading){
                  items(
                      count = 9,
                  ) {
                      TopicCardShimmerEffect()
                  }
                }else {
                    items(
                        items = state.topics
                    ){ topic ->

                        val timeInMin = if(user.settings.switchToCustomTimeInMin) customTimeInMin else topic.quizTimeInMin
                        PrimaryTopicCard(
                            imageRes = R.drawable.ic_app_transparent, // Replace with your image resource
                            topicName = topic.title(),
                            onTopicClick = {
                                if (topic.id.isNotEmpty()){
                                    onTopicClick(topic.id)
                                }
                            },
                            timeInMin = timeInMin,
                            showQuizTime = showQuizTime
                        )
                    }
                }



            }


        }
    }


}



@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    imageProfile: String? = null,
    onProfileClick:() -> Unit,
    onNotificationClick: () -> Unit,
    @DrawableRes icAvatar: Int,
    name: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = Modifier
                    .size(80.dp),
                onClick = onProfileClick
            ) {
                val image = if (imageProfile != null) imageProfile else icAvatar

                AsyncImage(
                    model = image,
                    contentDescription = "Image Profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
//                Image(
//                    painter = painterResource(icAvatar), // Placeholder for user avatar
//                    contentDescription = name,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(CircleShape)
//                        .background(GrayText) // Placeholder background for avatar
//                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        IconButton(
            modifier = Modifier
                .size(50.dp),
            onClick = onNotificationClick
        ) {
            Icon(
                imageVector = Icons.Default.Notifications, // Placeholder for user avatar
                contentDescription = "Icon notification",
                tint = Color.White
            )
        }



    }
}

@Composable
private fun StatisticsOverview(
    modifier: Modifier = Modifier,
    totalQuizzes: Int,
    correctAnswers: Int,
    accurateRate: Int
) {
    // Animatable states for each value
    val animatedTotalQuizzes by animateIntAsState(
        targetValue = totalQuizzes,
        animationSpec = tween(durationMillis = 10, easing = LinearOutSlowInEasing)
    )
    val animatedCorrectAnswers by animateIntAsState(
        targetValue = correctAnswers,
        animationSpec = tween(durationMillis = 10, easing = LinearOutSlowInEasing)
    )

    val animatedAccurateRateFloat by animateFloatAsState(
        targetValue = accurateRate.toFloat() / 100f, // Convert Int percentage to Float 0.0-1.0
        animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatisticItem(
            label = stringResource(R.string.total_quizzes),
            value = animatedTotalQuizzes,
        )
        AccurateRateIndicator(
            progress = animatedAccurateRateFloat // Use the converted animated float value
        )
        StatisticItem(
            label = stringResource(R.string.correct_answers),
            value = animatedCorrectAnswers,
        )
    }
}


@Composable
private fun StatisticItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = GrayText, fontSize = 14.sp)
        val animatedValue by animateIntAsState(
            targetValue = value,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
        Text(
            text = "$animatedValue",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}


@Composable
fun AccurateRateIndicator(progress: Float) {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = CardBackground,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx())
            )
        }
        // Progress arc
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                brush = Brush.sweepGradient(listOf(GreenAccent, LightPurple)),
                startAngle = -90f, // Start from top
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx())
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.accurate_rate), color = GrayText, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "${(progress * 100).toInt()}%",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
private fun NewVersionUpdateCard(
    modifier: Modifier = Modifier,
    versionName: String,
    releaseNotes: String,
    onUpdateButtonClicked: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground), // Example color
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Header Row: Version and Update Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedLoadingDotsText(
                    text = stringResource(R.string.new_update_available, versionName),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onUpdateButtonClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Update", color = CardBackground, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Expand/Collapse Toggle Text
            Text(
                text = if (isExpanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp)
            )

            // Collapsible Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    HorizontalDivider(thickness = 1.dp, color = Color.White.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.what_s_new),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = releaseNotes,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val topics =List(5){ index ->
        Topic(
            id = index.toString(),
            titleEnglish = "Architecture and Design $index",
            titleArabic = " الهندسة المعمارية والتصميم $index",
            subtitleArabic = "إبداع في البناء والجمال",
            subtitleEnglish = "Creativity in construction and beauty",
            masterOwnerId = "",
            quizTimeInMin = 45
        )
    }



    val user = User(
        gender = Gender.Male.name,
        userName = "Mamon Aburawi",
        password = "",
        email = ""
    )
    val appRelease = AppRelease(
        versionName = "1.0.0",
        versionCode = 1
    )
    val state = HomeState(
        user = user,
        totalQuizzies = 21,
        correctAnswers = 50,
        accurateRate = 86,
        topics = topics,
        isTopicLoading = false,
        appRelease = appRelease
    )


    HomeContent(
        state = state,
        onProfileClick = {},
        onPopularQuizzesClick = {},
        onTopViewedQuizzesClick = {},
        onTopicClick = {},
        onResumeQuizClick = {},
        onRefresh = {},
        onNotificationClick = {},
        onUpdateClicked = {}
    )


}


@Preview
@Composable
private fun NewVersionUpdateCardPreview(modifier: Modifier = Modifier) {
    NewVersionUpdateCard(
        versionName = "1.0.0",
        releaseNotes = "What's New in this Update:\n" +
                "\n" +
                "[New Feature Name]: A brief, benefit-oriented description of the new feature.\n" +
                "\n" +
                "Example: New Dark Mode: We've added a beautiful dark mode option to reduce eye strain and save battery life. You can enable it in the settings menu.\n" +
                "\n" +
                "[Another Feature or Improvement]:\n" +
                "\n" +
                "Example: Improved Performance: We've made significant under-the-hood optimizations to make the app faster and more responsive, especially when loading data.\n" +
                "\n" +
                "Key Bug Fixes:\n" +
                "\n" +
                "We've listened to your feedback and addressed some key issues to ensure a smoother experience.\n" +
                "\n" +
                "Fixed an issue where [specific bug description].\n" +
                "\n" +
                "Resolved a crash that occurred when [action that caused the crash].\n" +
                "\n" +
                "Corrected a display problem with [UI element or screen].\n" +
                "\n" +
                "We hope you enjoy these updates! If you have any feedback or suggestions, please don't hesitate to reach out.\n" +
                "\n" +
                "Happy [App Name]ing!",
        onUpdateButtonClicked = {}
    )
}
