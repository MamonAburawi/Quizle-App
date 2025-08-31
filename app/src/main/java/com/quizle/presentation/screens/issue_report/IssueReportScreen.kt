package com.quizle.presentation.screens.issue_report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.domain.module.QuizQuestion
import com.quizle.presentation.common.ErrorScreen
import com.quizle.presentation.common.LoadingButton
import com.quizle.presentation.common.LoadingScreen
import com.quizle.presentation.common.MessageType
import com.quizle.presentation.common.RadioItem
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.common.rememberToastMessageController
import com.quizle.presentation.navigation.back
import com.quizle.presentation.common.PrimaryAppBar
import com.quizle.presentation.common.QuestionCard
import com.quizle.presentation.theme.DarkBackground
import com.quizle.presentation.util.IssueType
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
        onBack = {
            navController.back()
        },
        onToastMessage = { message, type ->
            toastMessageController.showToast(message, type)
        }
    )
}


@Composable
private fun IssueReportContent(
    state: IssueReportState = IssueReportState(),
    onAction: (IssueReportAction) -> Unit,
    event: Flow<IssueReportEvent>,
    onBack:()-> Unit,
    onToastMessage:(String, MessageType) -> Unit
) {
    val quizQuestion = state.quizQuestion


    LaunchedEffect(key1 = Unit) {
        event.collect{ event ->
            when(event){
                is IssueReportEvent.NavigateToQuizScreen -> {
                    onBack()
                }
                is IssueReportEvent.ShowToast -> {
                    val message = event.message
                    onToastMessage(message, event.messageType)
                }
            }
        }
    }
 Scaffold(
     modifier = Modifier
         .fillMaxSize()
         .statusBarsPadding()
 ) { innerPadding ->
     Column(
         modifier = Modifier
             .fillMaxSize()
             .padding(innerPadding)
             .background(Color.White)
     ) {

         PrimaryAppBar(
             contentColor = Color.White,
             containerColor = DarkBackground,
             onBack = { onAction(IssueReportAction.BackButtonClicked) },
             title = stringResource(R.string.report_an_issue)
         )

         Column(
             modifier = Modifier
                 .fillMaxWidth()
                 .weight(0.9f)
         ) {
             if (!state.isReportLoading && state.error == null && quizQuestion != null){
                 Column(
                     modifier = Modifier
                         .fillMaxSize()
                         .weight(0.9f)
                         .verticalScroll(
                             state = rememberScrollState()
                         )
                         .padding(15.dp)
                 ) {

                     QuestionCard(
                         modifier = Modifier,
                         question = quizQuestion.questionText,
                         options = quizQuestion.allOptions,
                         explanation = quizQuestion.explanation
                     )


                     IssueTypeSection(
                         onItemSelected = {
                             onAction(IssueReportAction.OnIssueTypeSelected(it))
                         },
                         issueType = IssueType.fromKey(state.issueType),
                         otherText = if (IssueType.fromKey(state.issueType) == IssueType.Other(state.issueType)) state.issueType else "",
                         onOtherTextChanged = {
                             onAction(IssueReportAction.OtherTextChange(it))
                         }
                     )

                     Spacer(modifier = Modifier.height(20.dp))
                     TextField(
                         modifier = Modifier
                             .fillMaxWidth()
                             .clip(MaterialTheme.shapes.medium)
                             .height(200.dp),
                         maxLines = 10,
                         minLines = 1,
                         singleLine = false,
                         colors = TextFieldDefaults.colors(
                             focusedIndicatorColor = Color.Transparent,
                             unfocusedIndicatorColor = Color.Transparent,
                             disabledIndicatorColor = Color.Transparent
                         ),
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

                         }
                     )
                     Spacer(modifier = Modifier.height(5.dp))

                     Text(
                         modifier = Modifier.padding(horizontal = 10.dp),
                         text = stringResource(R.string.describe_the_issue_in_more_detail_optional),
                         color = Color.Gray,
                         fontSize = MaterialTheme.typography.bodySmall.fontSize
                     )

                     Spacer(modifier = Modifier.height(20.dp))
//
                 }
             }
             else{
                 if (state.error == null && state.isReportLoading){
                     LoadingScreen(
                         modifier = Modifier
                             .fillMaxSize(),
                         message = state.loadingMessage
                     )
                 }

             }
         }



         LoadingButton(
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(horizontal = 50.dp, vertical = 10.dp),
             text = stringResource(R.string.submit_report),
             isLoading = state.submitReportLoading,
             color = DarkBackground,
             onClick = {
                 onAction(IssueReportAction.OnSubmit)
             }
         )

         if (state.error != null){
             ErrorScreen(
                 modifier = Modifier.fillMaxSize(),
                 error = state.error,
                 onRefresh = { onAction(IssueReportAction.Refresh)}
             )
         }

     }
 }

}


@Composable
fun IssueTypeSection(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.issue_type),
    issueType: IssueType,
    otherText: String,
    onOtherTextChanged: (String) -> Unit,
    onItemSelected: (IssueType) -> Unit
) {
//    var selectedIssueType: IssueType by remember { mutableStateOf(IssueType.IncorrectAnswer) }
//    var otherText by remember { mutableStateOf("") } // State for the "Other" text field


    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        )

        RadioItem(
            option = stringResource(R.string.incorrect_answer),
            isSelected = issueType == IssueType.IncorrectAnswer,
            onSelectedItem = {
                onItemSelected(IssueType.IncorrectAnswer)
            }
        )
        RadioItem(
            option = stringResource(R.string.question_is_unclear),
            isSelected = issueType == IssueType.QuestionUnclear,
            onSelectedItem = {
                onItemSelected(IssueType.QuestionUnclear)
            }
        )
        RadioItem(
            option = stringResource(R.string.type_or_grammar_mistaken),
            isSelected = issueType == IssueType.TypoGrammarMistake,
            onSelectedItem = {
                onItemSelected(IssueType.TypoGrammarMistake)
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioItem(
                option = "",
                isSelected = issueType == IssueType.Other(otherText) || otherText.isNotEmpty(),
                onSelectedItem = {
                    onItemSelected(IssueType.Other(otherText))
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                value = otherText,
                singleLine = true,
                maxLines = 1,
                onValueChange = {
                    onOtherTextChanged(it)
                },
                label = {
                    Text(
                        text = "Other",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = Color.Gray
                    )

                }
            )
        }


    }
}


@Preview (showBackground = true)
@Composable
private fun IssueReportPreview() {
    val state = IssueReportState(
        QuizQuestion(
            questionText = "What is the capital of France?",
            allOptions = listOf("Paris", "London", "Berlin", "Madrid"),
            explanation = "Paris is the capital of France.",
            id = "1",
            correctAnswer = "",
            topicId = "5",
            masterOwnerId = ""
        ),
        issueType = IssueType.Other("").key,
        additionalComment = "",
        isReportLoading = false,
        error = null,
        loadingMessage = "loading data please wait..",
    )
    IssueReportContent(
        state = state,
        onBack = {},
        event = emptyFlow(),
        onAction = {},
        onToastMessage = {_,_ ->}
    )
}