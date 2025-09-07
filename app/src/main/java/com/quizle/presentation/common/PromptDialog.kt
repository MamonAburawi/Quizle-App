package com.quizle.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PromptDialog(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    positiveBtnText: String,
    negativeBtnText: String? = null,
    isOpen: Boolean = false,
    dialogColor: Color = Color.White,
    backgroundColor: Color = Color.Transparent.copy(alpha = 0.4f),
    onPositiveBtnClicked: () -> Unit,
    onCancel: () -> Unit = {}
) {
    if (isOpen){
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .clickable(enabled = false){},
            contentAlignment = Alignment.Center
        ){

            Card(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.8f),
                colors = CardDefaults.cardColors(contentColor = dialogColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = subTitle,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(60.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (negativeBtnText != null){
                            Text(
                                modifier = Modifier
                                    .clickable { onCancel() },
                                text = negativeBtnText,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                        }

                        Text(
                            modifier = Modifier
                                .clickable { onPositiveBtnClicked() },
                            text = positiveBtnText,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )


                    }
                }

            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun QuizDialogPreview(
    modifier: Modifier = Modifier
) {
    PromptDialog(
        title = "Submit Quiz",
        subTitle = "Are you sure you want to submit the quiz?",
        isOpen = true,
        onCancel = {},
        onPositiveBtnClicked = {},
        positiveBtnText = "Submit",
        negativeBtnText = "Cancel"
    )
}