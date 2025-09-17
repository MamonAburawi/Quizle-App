@file:OptIn(ExperimentalMaterial3Api::class)

package com.quizle.presentation.screens.settings

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.presentation.common.PrimaryButton
import com.quizle.presentation.common.SecondaryButton
import com.quizle.presentation.common.TextFieldBox
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.navigateToLogin
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.error
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.util.LocaleHelper
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
        onLanguageSelected = { onAction(SettingsAction.LanguageButtonClicked(it)) },
        onSaveClicked = { onAction(SettingsAction.SaveButtonClicked) },
        onQuizTimeEnabled = { onAction(SettingsAction.EnableQuizTimeButtonClicked) },
        onCustomTimeEnabled = { onAction(SettingsAction.EnableCustomTimeButtonClicked) },
        onCustomTimeInMinChanged = { onAction(SettingsAction.CustomTimeChanged(it)) }
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
                is SettingsEvent.ApplySettings -> {
                    LocaleHelper.setLocale(context, it.language)
                    (context as? Activity)?.recreate()
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
    onCustomTimeEnabled: () -> Unit,
    onCustomTimeInMinChanged: (Int) -> Unit
) {
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
    val langRes = if (state.selectedLanguage == "en") R.string.english else R.string.arabic
    val isDarkTheme = isSystemInDarkTheme()

    val backgroundBrush = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.extendedColors.surface,
                MaterialTheme.extendedColors.background
            )
        )
    } else {
        Brush.verticalGradient(colors = listOf(MaterialTheme.extendedColors.background, MaterialTheme.extendedColors.background))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(bottom = 16.dp)
    ) {
        TopAppBar(
            windowInsets = WindowInsets(0.dp),
            title = { Text(stringResource(R.string.settings), fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.extendedColors.onSurface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle(title = stringResource(R.string.quiz))

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.extendedColors.surface)
            ) {
                SettingItem(
                    title = stringResource(R.string.enable_quiz_time),
                    description = stringResource(R.string.quiz_time_toggle_description)
                ) {
                    Switch(
                        checked = state.isQuizTimeEnabled,
                        onCheckedChange = { onQuizTimeEnabled() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.extendedColors.textPrimary,
                            checkedTrackColor = MaterialTheme.extendedColors.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.extendedColors.onSurface
                        )
                    )
                }

                Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp)

                SettingItem(title = stringResource(R.string.use_custom_quiz_time)) {
                    Switch(
                        checked = state.isEnableCustomTimeSwitch,
                        onCheckedChange = { onCustomTimeEnabled() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.extendedColors.textPrimary,
                            checkedTrackColor = MaterialTheme.extendedColors.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.extendedColors.onSurface
                        )
                    )
                }

                AnimatedVisibility(visible = state.isEnableCustomTimeSwitch) {
                    TextFieldBox(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        value = state.customTimeInMinutes.toString(),
                        onValueChange = { newValue ->
                            if (newValue.isNotEmpty() && newValue.isDigitsOnly()) {
                                onCustomTimeInMinChanged(newValue.toInt())
                            } else if (newValue.isEmpty()) {
                                onCustomTimeInMinChanged(0)
                            }
                        },
                        hint = "Time",
                        keyboardType = KeyboardType.Number,
                        suffix = {
                            Text(
                                text = stringResource(R.string.min),
                                color = MaterialTheme.extendedColors.primary.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(title = stringResource(R.string.general))

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.extendedColors.surface)
            ) {
                SettingItem(title = stringResource(R.string.language)) {
                    Box {
                        Row(
                            Modifier.clickable { isLanguageMenuExpanded = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(langRes), color = MaterialTheme.extendedColors.onSurface)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Select Language",
                                tint = MaterialTheme.extendedColors.onSurface,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = isLanguageMenuExpanded,
                            onDismissRequest = { isLanguageMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.english),
                                        color = MaterialTheme.extendedColors.primary
                                    )
                                },
                                onClick = {
                                    isLanguageMenuExpanded = false
                                    onLanguageSelected("en")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(
                                    text = stringResource(R.string.arabic),
                                    color = MaterialTheme.extendedColors.primary
                                )
                                },
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

            PrimaryButton(
                isLoading = state.isLoadingSettings,
                text = stringResource(R.string.save_changes),
                onClick = onSaveClicked,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                onClick = onLogoutClicked,
                text = stringResource(R.string.logout),
                contentColor = Color.error,
                outLineColor = Color.error,
                isLoading = state.isLoggingOut,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
fun SettingItem(
    title: String,
    description: String? = null,
    control: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.extendedColors.onSurface,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            if (description != null) {
                Text(
                    text = description,
                    color = MaterialTheme.extendedColors.onSurface,
                    fontSize = 12.sp
                )
            }
        }
        control()
    }
}

@Preview(name = "Settings Screen Light Mode",showBackground = true)
@Composable
private fun SettingsScreenLightPreview() {
    val state = SettingsState(
        isQuizTimeEnabled = true,
        isEnableCustomTimeSwitch = true,
        customTimeInMinutes = 0
    )
    // Wrap the preview in your theme to see the result
    QuizleTheme(darkTheme = false) {
        SettingsContent(
            onLogoutClicked = {},
            onSaveClicked = {},
            onLanguageSelected = {},
            state = state,
            onQuizTimeEnabled = {},
            onCustomTimeEnabled = {},
            onCustomTimeInMinChanged = {},
        )
    }
}
@Preview(name = "Settings Screen Dark Mode",showBackground = true)
@Composable
private fun SettingsScreenDarkPreview() {
    val state = SettingsState(
        isQuizTimeEnabled = true,
        isEnableCustomTimeSwitch = true,
        customTimeInMinutes = 5
    )

    QuizleTheme(darkTheme = true) {
        SettingsContent(
            onLogoutClicked = {},
            onSaveClicked = {},
            onLanguageSelected = {},
            state = state,
            onQuizTimeEnabled = {},
            onCustomTimeEnabled = {},
            onCustomTimeInMinChanged = {},
        )
    }
}

