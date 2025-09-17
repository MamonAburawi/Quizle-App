@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("KotlinConstantConditions")

package com.quizle.presentation.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.quizle.R
import com.quizle.data.utils.Gender
import com.quizle.domain.module.AppRelease
import com.quizle.domain.module.Topic
import com.quizle.domain.module.User
import com.quizle.presentation.common.*
import com.quizle.presentation.navigation.*
import com.quizle.presentation.theme.Color
import com.quizle.presentation.theme.Color.SemanticBlue
import com.quizle.presentation.theme.Color.SemanticGreen
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.util.openUrlInBrowser
import com.quizle.presentation.util.title
import kotlinx.coroutines.flow.Flow

// HomeScreen and its logic remain largely the same, only HomeContent is changed.
@Composable
fun HomeScreen(
    state: HomeState,
    appNavController: NavHostController,
    onAction: (HomeAction) -> Unit,
    event: Flow<HomeEvent>,
    toastManager: ToastMessageController
) {

    val context = LocalContext.current

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

    HomeContent(
        state = state,
        onProfileClick = { appNavController.navigateToProfile() },
        onTopicClick = { onAction(HomeAction.TopicItemCardClicked(it)) },
        onRefresh = { onAction(HomeAction.OnRefresh) },
        onNotificationClick = { onAction(HomeAction.NotificationButtonClicked) },
        onUpdateClicked = { onAction(HomeAction.UpdateButtonClicked) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    state: HomeState,
    onProfileClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onRefresh: () -> Unit,
    onUpdateClicked: () -> Unit
) {
    val user = state.user
    val icAvatar = if (user.gender == Gender.Male.name) R.drawable.ic_male_avatar else R.drawable.ic_female_avatar

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        state = rememberPullToRefreshState(),
        onRefresh = onRefresh
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.extendedColors.background),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            profileSection(
                imageProfile = user.imageProfile,
                onProfileClick = onProfileClick,
                onNotificationClick = onNotificationClick,
                icAvatar = icAvatar,
                name = user.userName
            )
            statisticsOverview(
                totalQuizzes = state.totalQuizzies,
                correctAnswers = state.correctAnswers,
                accurateRate = state.accurateRate
            )
            newVersionUpdateCard(
                modifier = Modifier.padding(vertical = 5.dp),
                isUpdateAvailable = state.isNewAppUpdatesAvailable,
                versionName = state.appRelease.versionName,
                releaseNotes = state.appRelease.releaseNoteEn,
                onUpdateButtonClicked = onUpdateClicked
            )
            topicSection(
                isLoading = state.isTopicLoading,
                onTopicClick = onTopicClick,
                topics = state.topics,
                isCustomTimeEnabled = user.settings.switchToCustomTimeInMin,
                customTimeInMin = user.settings.customQuizTimeInMin,
                showQuizTime = user.settings.enableQuizTime
            )
        }
    }
}

private fun LazyGridScope.profileSection(
    imageProfile: String?,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    @DrawableRes icAvatar: Int,
    name: String,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(modifier = Modifier.size(64.dp), onClick = onProfileClick) {
                    AsyncImage(
                        model = imageProfile ?: icAvatar,
                        contentDescription = "Image Profile",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = name,
                    // NEW: Theme-aware color
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Icon notification",
                    // NEW: Theme-aware color
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

private fun LazyGridScope.statisticsOverview(
    totalQuizzes: Int,
    correctAnswers: Int,
    accurateRate: Int
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatisticItem(label = stringResource(R.string.total_quizzes), value = totalQuizzes)
            AccurateRateIndicator(progress = accurateRate.toFloat() / 100f)
            StatisticItem(label = stringResource(R.string.correct_answers), value = correctAnswers)
        }
    }
}

@Composable
private fun StatisticItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        val animatedValue by animateIntAsState(targetValue = value, animationSpec = tween(1000), label = "")
        Text(
            text = "$animatedValue",
            // NEW: Theme-aware color
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AccurateRateIndicator(progress: Float) {
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000), label = "")
        // Track
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.BackGroundDark,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx())
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                // NEW: Theme-aware gradient
                brush = Brush.sweepGradient(
                    listOf(
                        SemanticGreen,
                        SemanticBlue
                    )
                ),
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx())
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.accurate_rate),
                // NEW: Theme-aware color
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                // NEW: Theme-aware color
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun LazyGridScope.newVersionUpdateCard(
    modifier: Modifier = Modifier,
    isUpdateAvailable: Boolean,
    versionName: String,
    releaseNotes: String,
    onUpdateButtonClicked: () -> Unit,
) {
    if (isUpdateAvailable) {
        item(
            span = { GridItemSpan(maxLineSpan) }) {
            var isExpanded by remember { mutableStateOf(false) }
            Card(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.new_update_available, versionName),
                            // NEW: Theme-aware color
                            color = MaterialTheme.extendedColors.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        // NEW: Themed button
                        Button(
                            onClick = onUpdateButtonClicked,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.extendedColors.primary,
                                contentColor = MaterialTheme.extendedColors.textPrimary
                            )
                        ) {
                            Text(text = "Update", fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(
                        text = if (isExpanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
                        // NEW: Theme-aware color
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { isExpanded = !isExpanded }.padding(vertical = 8.dp)
                    )
                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.what_s_new),
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = releaseNotes,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(name = "Home Screen - Light", showBackground = true)
@Composable
private fun HomeScreenLightPreview() {
    QuizleTheme(darkTheme = false) {
        val state = HomeState(
            user = User(userName = "Mamon Aburawi", gender = "Male"),
            totalQuizzies = 21, correctAnswers = 50, accurateRate = 86,
            topics = List(6) { Topic(titleEnglish = "Topic $it") },
            isTopicLoading = false,
            isNewAppUpdatesAvailable = true,
            appRelease = AppRelease(versionName = "1.0.1", releaseNoteEn = "Bug fixes and improvements.")
        )
        HomeContent(state, {}, {}, {}, {}, {})
    }
}

@Preview(name = "Home Screen Loading - Dark", showBackground = true)
@Composable
private fun HomeScreenLoadingDarkPreview() {
    QuizleTheme(darkTheme = true) {
        val state = HomeState(
            user = User(userName = "Mamon Aburawi", gender = "Male"),
            isTopicLoading = true,
            isNewAppUpdatesAvailable = false
        )
        HomeContent(state, {}, {}, {}, {}, {})
    }
}

private fun LazyGridScope.topicSection(
    isLoading: Boolean,
    onTopicClick: (String) -> Unit,
    topics: List<Topic>,
    shimmerItemCount: Int = 10,
    isCustomTimeEnabled: Boolean,
    customTimeInMin: Int,
    showQuizTime: Boolean,
){
    if (isLoading){
        items(
            count = shimmerItemCount,
        ) {
            TopicCardShimmerEffect()
        }
    }else {
        items(
            items = topics
        ){ topic ->

            val timeInMin = if(isCustomTimeEnabled) customTimeInMin else topic.quizTimeInMin
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
