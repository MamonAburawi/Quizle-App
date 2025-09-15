package com.quizle.presentation


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.quizle.presentation.common.ToastHost
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.NestedNavGraph
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.util.LocaleHelper


class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            val updatedContext = LocaleHelper.updateContext(newBase)
            super.attachBaseContext(updatedContext)
        } else {
            super.attachBaseContext(newBase)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        val sharePref = AppPreferences(this)
//        this.baseContext.setAppLanguage(sharePref.loadSettings().language)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
           AppRoot()
        }
    }




}


@Composable
fun AppRoot() {
    val navHost = rememberNavController()

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val toastMessageController = remember {
        ToastMessageController(snackBarHostState, coroutineScope)
    }

    QuizleTheme {
//        Scaffold{ innerPadding ->
            Box(
                modifier = Modifier
//                    .padding(innerPadding)
                    .fillMaxSize()
            ){
                NestedNavGraph(
                    modifier = Modifier,
                    navController = navHost,
                    toastMessageController = toastMessageController
                )
                ToastHost(
                    modifier = Modifier
                        .padding(top = 15.dp),
                    snackBarHostState = snackBarHostState
                )
            }
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizleTheme {
        AppRoot()
    }
}

