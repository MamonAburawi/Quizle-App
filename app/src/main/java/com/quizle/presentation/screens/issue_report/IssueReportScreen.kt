package com.quizle.presentation.screens.issue_report


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.data.utils.IssueType
import com.quizle.domain.module.Question
import com.quizle.presentation.common.*
import com.quizle.presentation.navigation.back
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.theme.unSelected
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun IssueReportScreen(
    navController: NavHostController,
    state: IssueReportState,
    toastMessageController: ToastMessageController,
    onAction: (IssueReportAction) -> Unit,
    event: Flow<IssueReportEvent>
) {
    IssueReportContent(
        state = state,
        onAction = onAction,
        event = event,
        onBack = { navController.back() },
        onToastMessage = { message, type -> toastMessageController.showToast(message, type) }
    )
}

@Composable
private fun IssueReportContent(
    state: IssueReportState,
    onAction: (IssueReportAction) -> Unit,
    event: Flow<IssueReportEvent>,
    onBack: () -> Unit,
    onToastMessage: (String, MessageType) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        event.collect { event ->
            when (event) {
                is IssueReportEvent.NavigateToQuizScreen -> onBack()
                is IssueReportEvent.ShowToast -> onToastMessage(event.message, event.messageType)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        topBar = {
            // NEW: Themed app bar, no hardcoded colors
            PrimaryAppBar(
                onBack = { onAction(IssueReportAction.BackButtonClicked) },
                title = stringResource(R.string.report_an_issue)
            )
        }
    ) { innerPadding ->
        // NEW: Removed hardcoded background. Scaffold provides the correct theme background.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isReportLoading -> {
                    LoadingScreen(
                        modifier = Modifier.fillMaxSize(),
                        isLoading = true,
                        initMessage = state.loadingMessage
                    )
                }
                state.error != null -> {
                    ErrorScreen(
                        modifier = Modifier.fillMaxSize(),
                        error = state.error,
                        onRefresh = { onAction(IssueReportAction.Refresh) }
                    )
                }
                state.question != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Takes up available space
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        QuestionCard(
                            question = state.question.questionText,
                            options = state.question.allOptions,
                            explanation = state.question.explanation
                        )
                        IssueTypeSection(
                            issueType = IssueType.fromKey(state.issueType),
                            otherText = if (IssueType.fromKey(state.issueType) is IssueType.Other) state.issueType else "",
                            onOtherTextChanged = { onAction(IssueReportAction.OtherTextChange(it)) },
                            onItemSelected = { onAction(IssueReportAction.OnIssueTypeSelected(it)) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .height(200.dp),
                            maxLines = 10,
                            minLines = 1,
                            singleLine = false,
                            value = state.additionalComment ?: "",
                            onValueChange = {
                                onAction(IssueReportAction.OnAdditionalCommentChanged(it))
//                        additionalComment = it
                            },
                            label = {
                                Text(
                                    text = stringResource(R.string.additional_comment),
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    color = Color.Gray
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                cursorColor = MaterialTheme.extendedColors.primaryColor,
                                focusedIndicatorColor = MaterialTheme.extendedColors.primaryColor,
                                unfocusedIndicatorColor = MaterialTheme.extendedColors.primaryColor,
                                unfocusedContainerColor = MaterialTheme.extendedColors.onSurfaceColor.copy(
                                    alpha = 0.7f
                                ),
                                focusedContainerColor = MaterialTheme.extendedColors.onSurfaceColor,
                                unfocusedLabelColor = MaterialTheme.extendedColors.primaryColor.copy(
                                    alpha = 0.8f
                                ),
                                focusedLabelColor = MaterialTheme.extendedColors.primaryColor.copy(
                                    alpha = 0.8f
                                ),
                                focusedTextColor = MaterialTheme.extendedColors.primaryColor,
                                unfocusedTextColor = MaterialTheme.extendedColors.primaryColor.copy(
                                    alpha = 0.8f
                                )
                            )
                        )



                    }


                    LoadingButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = stringResource(R.string.submit_report),
                        isLoading = state.submitReportLoading,
                        onClick = { onAction(IssueReportAction.OnSubmit) }
                    )
                }
            }
        }
    }
}

@Composable
fun IssueTypeSection(
    modifier: Modifier = Modifier,
    issueType: IssueType,
    otherText: String,
    onOtherTextChanged: (String) -> Unit,
    onItemSelected: (IssueType) -> Unit
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.issue_type),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        RadioItem(
            option = stringResource(R.string.incorrect_answer),
            isSelected = issueType is IssueType.IncorrectAnswer,
            onSelectedItem = { onItemSelected(IssueType.IncorrectAnswer) }
        )
        RadioItem(
            option = stringResource(R.string.question_is_unclear),
            isSelected = issueType is IssueType.QuestionUnclear,
            onSelectedItem = { onItemSelected(IssueType.QuestionUnclear) }
        )
        RadioItem(
            option = stringResource(R.string.type_or_grammar_mistaken),
            isSelected = issueType is IssueType.TypoGrammarMistake,
            onSelectedItem = { onItemSelected(IssueType.TypoGrammarMistake) }
        )
        Column {
            val otherFieldEmpty = issueType is IssueType.Other && issueType.key.isEmpty()

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioItem(
                    selectedColor = MaterialTheme.extendedColors.onSurfaceColor,
                    option = "",
                    isSelected = issueType is IssueType.Other,
                    onSelectedItem = { onItemSelected(IssueType.Other(otherText)) }
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = otherText,
                    onValueChange = {
                        onOtherTextChanged(it)
                        onItemSelected(IssueType.Other(it))
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.issue_type_other_label),
                            color = MaterialTheme.extendedColors.onSurfaceColor.copy(alpha = 0.8f)
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                    isError = otherFieldEmpty,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.extendedColors.secondaryColor,
                        unfocusedIndicatorColor = Color.unSelected,
                        errorIndicatorColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )
            }

            if (otherFieldEmpty){
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Please enter the issue type",
                    color = MaterialTheme.extendedColors.error
                )
            }
        }

    }
}



@Preview(name = "Issue Report - Light Theme", showBackground = true)
@Composable
private fun IssueReportLightPreview() {
    QuizleTheme(darkTheme = false) {
        val state = IssueReportState(
            Question(
                questionText = "What is the capital of France?",
                allOptions = listOf("Paris", "London", "Berlin", "Madrid"),
                explanation = "Paris is the capital of France.",
                id = "1",
                correctAnswer = "",
                topicId = "5",
                masterOwnerId = ""
            ),
            issueType = IssueType.IncorrectAnswer.key,
            additionalComment = "",
            isReportLoading = false,
            error = null,
            loadingMessage = "loading data please wait..",
        )
        IssueReportContent(state, {}, emptyFlow(), {}, { _, _ -> })
    }
}


