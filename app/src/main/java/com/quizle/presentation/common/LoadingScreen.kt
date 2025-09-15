//package com.quizle.presentation.common
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.background
//import androidx.compose.material3.CircularProgressIndicator
//
//
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
package com.quizle.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.QuizleTheme


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    initMessage: String = "",
    background: Color = MaterialTheme.colorScheme.background,
    // The content color now defaults to the theme's onBackground color
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    AnimatedVisibility(
        visible = isLoading
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(25.dp),
                color = contentColor
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 5.dp),
                text = initMessage,
                color = contentColor,
                style = MaterialTheme.typography.titleMedium, // Use a style for consistency
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(name = "Loading Screen Light Mode", showBackground = true)
@Composable
fun LoadingScreenLightPreview() {
    QuizleTheme(darkTheme = false) {
        LoadingScreen(
            initMessage = "Questions initializing, Please wait...",
            isLoading = true
        )
    }
}

@Preview(name = "Loading Screen Dark Mode", showBackground = true)
@Composable
fun LoadingScreenDarkPreview() {
    QuizleTheme(darkTheme = true) {
        LoadingScreen(
            initMessage = "Questions initializing, Please wait...",
            isLoading = true
        )
    }
}

//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.quizle.presentation.theme.QuizleTheme
//
//@Composable
//fun LoadingScreen(
//    modifier: Modifier = Modifier,
//    background: Color = MaterialTheme.colorScheme.background,
//    isLoading: Boolean = false,
//    initMessage: String = "",
//    contentColor: Color = Color.White
//) {
//    AnimatedVisibility(
//        visible = isLoading
//    ){
//        Column(
//            modifier = modifier.fillMaxSize()
//                .background(background),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            CircularProgressIndicator(
//                modifier = Modifier.size(25.dp),
//                color = contentColor
//            )
//            Spacer(modifier = Modifier.height(5.dp))
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .padding(top =5.dp),
//                text = initMessage,
//                color = contentColor,
//                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
//                textAlign = TextAlign.Center
//            )
//
//        }
//    }
//
//
//
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun ErrorContentPreview() {
//    QuizleTheme {
//        LoadingScreen(
//            initMessage = "Questions initializing, Please wait...",
//            isLoading = true,
//            contentColor = Color.Black
//        )
//    }
//}
//
