@file:OptIn(ExperimentalMaterial3Api::class)

package com.quizle.presentation.screens.settings



import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.presentation.common.LoadingButton
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.navigateToLogin
import com.quizle.presentation.theme.DarkBackground
import com.quizle.presentation.theme.GreenAccent
import com.quizle.presentation.theme.SurfaceColor
import com.quizle.presentation.util.relaunchApp
import com.quizle.presentation.util.setAppLanguage
import kotlinx.coroutines.flow.Flow


@Composable
fun SettingsScreen(
    toastManager: ToastMessageController,
    appNavController: NavHostController,
    state: SettingsState,
    event: Flow<SettingsEvent>,
    onAction: (SettingsAction) -> Unit
) {

    val context = LocalContext.current
    SettingsContent(
        state = state,
        onLogoutClicked = { onAction(SettingsAction.LogoutButtonClicked) },
        onLanguageSelected = { onAction(SettingsAction.LanguageButtonClicked(it))},
        onSaveClicked = { onAction(SettingsAction.SaveButtonClicked)},
        onQuizTimeEnabled = { onAction(SettingsAction.EnableQuizTimeButtonClicked)},
        onCustomTimeEnabled = { onAction(SettingsAction.EnableCustomTimeButtonClicked)},
        onCustomTimeInMinChanged = { onAction(SettingsAction.CustomTimeChanged(it))}
    )


    LaunchedEffect(key1 = Unit) {
      event.collect {
          when(it){
              is SettingsEvent.ShowToast -> {
                  val type = it.type
                  val message = it.message
                  toastManager.showToast(message,type)
              }
              is SettingsEvent.NavigateToSignUp -> {
                  appNavController.navigateToLogin()
              }


              is SettingsEvent.RelaunchApp -> {
                  val langCode = it.language
                  context.apply {
                      setAppLanguage(langCode)
                      relaunchApp()
                  }
              }
          }
      }
    }



}
@SuppressLint("SuspiciousIndentation")
@Composable
fun SettingsContent(
    state: SettingsState,
    onLogoutClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    onQuizTimeEnabled: () -> Unit,
    onCustomTimeEnabled:() -> Unit,
    onCustomTimeInMinChanged:(Int) -> Unit

) {
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }

    val langRes = if (state.selectedLanguage == "en") R.string.english else R.string.arabic

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceColor)
        ) {

            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(R.string.settings)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = Color.White
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quiz Time Toggle
                SettingsCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.enable_quiz_time), fontWeight = FontWeight.Medium)
                        Switch(
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DarkBackground,
                                checkedTrackColor = GreenAccent
                            ),
                            checked = state.isQuizTimeEnabled,
                            onCheckedChange = { onQuizTimeEnabled() }
                        )
                    }
                }

                // Custom Time Switch and Input Field
                SettingsCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(R.string.use_custom_quiz_time), fontWeight = FontWeight.Medium)
                            Switch(
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DarkBackground,
                                    checkedTrackColor = GreenAccent
                                ),
                                checked = state.isEnableCustomTimeSwitch,
                                onCheckedChange = {onCustomTimeEnabled()}
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.customTimeInMinutes.toString(),
                            onValueChange = { newValue ->
                                if (newValue.isNotEmpty() && newValue.isDigitsOnly()) {
                                    onCustomTimeInMinChanged(newValue.toInt())
                                } else if (newValue.isEmpty()) {
                                    onCustomTimeInMinChanged(0)
                                }
                            },
                            suffix = {
                                Text(text = stringResource(R.string.min))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = state.isEnableCustomTimeSwitch
                        )
                    }
                }

                // Language Dropdown List
                SettingsCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.language),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopStart)
                        ) {
                            OutlinedButton(
                                onClick = { isLanguageMenuExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(langRes),
                                    modifier = Modifier.weight(1f),
                                    color = DarkBackground
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                            }
                            DropdownMenu(
                                expanded = isLanguageMenuExpanded,
                                onDismissRequest = { isLanguageMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.english)) },
                                    onClick = {
                                        isLanguageMenuExpanded = false
                                        onLanguageSelected("en")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.arabic)) },
                                    onClick = {
                                        isLanguageMenuExpanded = false
                                        onLanguageSelected("ar")
                                    }
                                )
                            }
                        }
                    }

            }

                Spacer(modifier = Modifier.weight(1f))

               

                LoadingButton(
                    isLoading = state.isLoading,
                    text = stringResource(R.string.save),
                    onClick = onSaveClicked,
                    color = GreenAccent,
                    textColor = DarkBackground,
                    fontWeight = FontWeight.Bold
                )
                
                // Logout Button
                Button(
                    onClick = onLogoutClicked,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(R.string.logout), color = MaterialTheme.colorScheme.onError)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


        }

}

@Composable
fun SettingsCard(
    content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    val state = SettingsState()
    SettingsContent(
        onLogoutClicked = {},
        onSaveClicked = {},
        onLanguageSelected = {},
        state = state,
        onQuizTimeEnabled = {},
        onCustomTimeEnabled = { TODO() },
        onCustomTimeInMinChanged = {},
    )
}

//@Composable
//fun SettingsScreen() {
//    SettingsContent()
//}
//
//@Composable
//private fun SettingsContent() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(DarkBackground),
//        contentAlignment = Alignment.Center
//    ) {
//
//        Text(
//            text = "Settings",
//            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun SettingsPreview() {
//    SettingsContent()
//}