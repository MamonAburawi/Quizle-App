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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.quizle.R
import com.quizle.presentation.common.LoadingButton
import com.quizle.presentation.common.ToastMessageController
import com.quizle.presentation.navigation.navigateToLogin
import com.quizle.presentation.theme.QuizleTheme
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

    // The background is now theme-aware
    val backgroundBrush = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.background
            )
        )
    } else {
        Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.background))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        TopAppBar(
            windowInsets = WindowInsets(0.dp),
            title = { Text(stringResource(R.string.settings), fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle(title = stringResource(R.string.settings))

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                SettingItem(
                    title = stringResource(R.string.enable_quiz_time),
                    description = ""
                ) {
                    Switch(
                        checked = state.isQuizTimeEnabled,
                        onCheckedChange = { onQuizTimeEnabled() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }

                Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp)

                SettingItem(title = stringResource(R.string.use_custom_quiz_time)) {
                    Switch(
                        checked = state.isEnableCustomTimeSwitch,
                        onCheckedChange = { onCustomTimeEnabled() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }

                AnimatedVisibility(visible = state.isEnableCustomTimeSwitch) {
                    OutlinedTextField(
                        value = state.customTimeInMinutes.toString(),
                        onValueChange = { newValue ->
                            if (newValue.isNotEmpty() && newValue.isDigitsOnly()) {
                                onCustomTimeInMinChanged(newValue.toInt())
                            } else if (newValue.isEmpty()) {
                                onCustomTimeInMinChanged(0)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        label = { Text(stringResource(R.string.enable_quiz_time)) },
                        suffix = { Text(stringResource(R.string.min)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(title = stringResource(R.string.general))

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                SettingItem(title = stringResource(R.string.language)) {
                    Box {
                        Row(
                            Modifier.clickable { isLanguageMenuExpanded = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(langRes), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Select Language",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
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
                text = stringResource(R.string.save_changes),
                onClick = onSaveClicked,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = onLogoutClicked,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Text(text = stringResource(R.string.logout), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            if (description != null) {
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        customTimeInMinutes = 5
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
    // Wrap the preview in your theme to see the result
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


///@file:OptIn(ExperimentalMaterial3Api::class)
//@file:OptIn(ExperimentalMaterial3Api::class)
//
//package com.quizle.presentation.screens.settings
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowForward
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.text.isDigitsOnly
//import androidx.navigation.NavHostController
//import com.quizle.R
//import com.quizle.presentation.common.LoadingButton
//import com.quizle.presentation.common.ToastMessageController
//import com.quizle.presentation.navigation.navigateToLogin
//import com.quizle.presentation.util.LocaleHelper
//import kotlinx.coroutines.flow.Flow
//
//// --- New Professional Color Palette ---
//
//
//// The SettingsScreen function remains the same as it handles logic, not UI.
//@Composable
//fun SettingsScreen(
//    toastManager: ToastMessageController,
//    appNavController: NavHostController,
//    state: SettingsState,
//    event: Flow<SettingsEvent>,
//    onAction: (SettingsAction) -> Unit
//) {
//    // ... (logic remains the same)
//    val context = LocalContext.current
//    EnhancedSettingsContent(
//        state = state,
//        onLogoutClicked = { onAction(SettingsAction.LogoutButtonClicked) },
//        onLanguageSelected = { onAction(SettingsAction.LanguageButtonClicked(it)) },
//        onSaveClicked = { onAction(SettingsAction.SaveButtonClicked) },
//        onQuizTimeEnabled = { onAction(SettingsAction.EnableQuizTimeButtonClicked) },
//        onCustomTimeEnabled = { onAction(SettingsAction.EnableCustomTimeButtonClicked) },
//        onCustomTimeInMinChanged = { onAction(SettingsAction.CustomTimeChanged(it)) }
//    )
//
//    LaunchedEffect(key1 = Unit) {
//      event.collect {
//          when(it){
//              is SettingsEvent.ShowToast -> {
//                  val type = it.type
//                  val message = it.message
//                  toastManager.showToast(message,type)
//              }
//              is SettingsEvent.NavigateToSignUp -> {
//                  appNavController.navigateToLogin()
//              }
//
//
//              is SettingsEvent.ApplySettings -> {
//                  LocaleHelper.setLocale(context, it.language)
//                  (context as? Activity)?.recreate()
//              }
//          }
//      }
//    }
//
//}
//
//@SuppressLint("SuspiciousIndentation")
//@Composable
//fun EnhancedSettingsContent(
//    state: SettingsState,
//    onLogoutClicked: () -> Unit,
//    onSaveClicked: () -> Unit,
//    onLanguageSelected: (String) -> Unit,
//    onQuizTimeEnabled: () -> Unit,
//    onCustomTimeEnabled: () -> Unit,
//    onCustomTimeInMinChanged: (Int) -> Unit
//) {
//    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
//    val langRes = if (state.selectedLanguage == "en") R.string.english else R.string.arabic
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Brush.verticalGradient(colors = listOf(DarkGradientStart, DarkGradientEnd)))
//    ) {
//        TopAppBar(
//            windowInsets = WindowInsets(0.dp),
//            title = { Text(stringResource(R.string.settings), fontWeight = FontWeight.Bold) },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.Transparent, // Transparent TopAppBar
//                titleContentColor = TextFieldColorPrimary
//            )
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp)
//                .verticalScroll(rememberScrollState()) // Allow scrolling for smaller screens
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // --- Quiz Settings Section ---
//            SectionTitle(title = stringResource(R.string.settings))
//
//            Column(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(ComponentBackground)
//            ) {
//                SettingItem(
//                    title = stringResource(R.string.enable_quiz_time),
//                    description = ""
//                ) {
//                    Switch(
//                        checked = state.isQuizTimeEnabled,
//                        onCheckedChange = { onQuizTimeEnabled() },
//                        colors = SwitchDefaults.colors(
//                            checkedThumbColor = Color.White,
//                            checkedTrackColor = MutedGreen,
//                            uncheckedThumbColor = Color.White,
//                            uncheckedTrackColor = Color.Gray
//                        )
//                    )
//                }
//
//                Divider(color = DarkGradientStart, thickness = 1.dp)
//
//                SettingItem(title = stringResource(R.string.use_custom_quiz_time)) {
//                    Switch(
//                        checked = state.isEnableCustomTimeSwitch,
//                        onCheckedChange = { onCustomTimeEnabled() },
//                        colors = SwitchDefaults.colors(
//                            checkedThumbColor = Color.White,
//                            checkedTrackColor = MutedGreen,
//                            uncheckedThumbColor = Color.White,
//                            uncheckedTrackColor = Color.Gray
//                        )
//                    )
//                }
//
//                // Custom time input field, shown within the section
//                AnimatedVisibility(visible = state.isEnableCustomTimeSwitch) {
//                    OutlinedTextField(
//                        value = state.customTimeInMinutes.toString(),
//                        onValueChange = { newValue ->
//                            if (newValue.isNotEmpty() && newValue.isDigitsOnly()) {
//                                onCustomTimeInMinChanged(newValue.toInt())
//                            } else if (newValue.isEmpty()) {
//                                onCustomTimeInMinChanged(0)
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp),
//                        label = { Text(stringResource(R.string.enable_quiz_time)) },
//                        suffix = { Text(stringResource(R.string.min)) },
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        colors = TextFieldDefaults.colors(
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = TextSecondary,
//                            cursorColor = MutedGreen,
//                            focusedIndicatorColor = MutedGreen,    // Mapped from focusedBorderColor
//                            unfocusedIndicatorColor = TextSecondary, // Mapped from unfocusedBorderColor
//                            unfocusedContainerColor = Color.Transparent, // Mapped from containerColor
//                            unfocusedLabelColor = TextSecondary,
//                            focusedLabelColor = TextFieldColorPrimary
//                        )
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // --- General Settings Section ---
//            SectionTitle(title = stringResource(R.string.general))
//
//            Column(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(ComponentBackground)
//            ) {
//                SettingItem(title = stringResource(R.string.language)) {
//                    Box {
//                        Row(
//                            Modifier.clickable { isLanguageMenuExpanded = true },
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(text = stringResource(langRes), color = TextSecondary)
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Icon(
//                                Icons.AutoMirrored.Filled.ArrowForward,
//                                contentDescription = "Select Language",
//                                tint = TextSecondary,
//                                modifier = Modifier.size(14.dp)
//                            )
//                        }
//                        DropdownMenu(
//                            expanded = isLanguageMenuExpanded,
//                            onDismissRequest = { isLanguageMenuExpanded = false }
//                        ) {
//                            DropdownMenuItem(
//                                text = { Text(stringResource(R.string.english)) },
//                                onClick = {
//                                    isLanguageMenuExpanded = false
//                                    onLanguageSelected("en")
//                                }
//                            )
//                            DropdownMenuItem(
//                                text = { Text(stringResource(R.string.arabic)) },
//                                onClick = {
//                                    isLanguageMenuExpanded = false
//                                    onLanguageSelected("ar")
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//
//
//            Spacer(modifier = Modifier.weight(1f)) // Pushes buttons to the bottom
//
//            LoadingButton(
//                isLoading = state.isLoading,
//                text = stringResource(R.string.save_changes),
//                onClick = onSaveClicked,
//                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                color = MutedGreen,
//                textColor = Color.White,
//                fontWeight = FontWeight.Bold
//            )
//
//            OutlinedButton(
//                onClick = onLogoutClicked,
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.outlinedButtonColors(contentColor = MutedRed),
//                border = BorderStroke(1.dp, MutedRed)
//            ) {
//                Text(text = stringResource(R.string.logout), fontWeight = FontWeight.Bold)
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//
///**
// * A reusable composable for section titles in the settings screen.
// */
//@Composable
//fun SectionTitle(title: String) {
//    Text(
//        text = title,
//        style = MaterialTheme.typography.titleSmall,
//        color = TextSecondary,
//        fontWeight = FontWeight.Bold,
//        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
//    )
//}
//
//
//@Composable
//fun SettingItem(
//    title: String,
//    description: String? = null,
//    control: @Composable () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = title,
//                color = TextFieldColorPrimary,
//                fontWeight = FontWeight.Medium,
//                fontSize = 16.sp
//            )
//            if (description != null) {
//                Text(
//                    text = description,
//                    color = TextSecondary,
//                    fontSize = 12.sp
//                )
//            }
//        }
//        control()
//    }
//}
//
//@Preview(showBackground = true, backgroundColor = 0xFF1B2631)
//@Composable
//private fun EnhancedSettingsPreview() {
//    val state = SettingsState(
//        isQuizTimeEnabled = true,
//        isEnableCustomTimeSwitch = false,
//        customTimeInMinutes = 5
//    )
//    EnhancedSettingsContent(
//        onLogoutClicked = {},
//        onSaveClicked = {},
//        onLanguageSelected = {},
//        state = state,
//        onQuizTimeEnabled = {},
//        onCustomTimeEnabled = {},
//        onCustomTimeInMinChanged = {},
//    )
//}
////package com.quizle.presentation.screens.settings
////
////
////
////import android.annotation.SuppressLint
////import android.app.Activity
////import androidx.compose.foundation.background
////import androidx.compose.foundation.layout.Arrangement
////import androidx.compose.foundation.layout.Box
////import androidx.compose.foundation.layout.Column
////import androidx.compose.foundation.layout.Row
////import androidx.compose.foundation.layout.Spacer
////import androidx.compose.foundation.layout.WindowInsets
////import androidx.compose.foundation.layout.fillMaxSize
////import androidx.compose.foundation.layout.fillMaxWidth
////import androidx.compose.foundation.layout.height
////import androidx.compose.foundation.layout.padding
////import androidx.compose.foundation.layout.wrapContentSize
////import androidx.compose.foundation.shape.RoundedCornerShape
////import androidx.compose.foundation.text.KeyboardOptions
////import androidx.compose.material.icons.Icons
////import androidx.compose.material.icons.filled.ArrowDropDown
////import androidx.compose.material3.Button
////import androidx.compose.material3.ButtonDefaults
////import androidx.compose.material3.Card
////import androidx.compose.material3.CardDefaults
////import androidx.compose.material3.DropdownMenu
////import androidx.compose.material3.DropdownMenuItem
////import androidx.compose.material3.ExperimentalMaterial3Api
////import androidx.compose.material3.Icon
////import androidx.compose.material3.MaterialTheme
////import androidx.compose.material3.OutlinedButton
////import androidx.compose.material3.OutlinedTextField
////import androidx.compose.material3.Switch
////import androidx.compose.material3.SwitchDefaults
////import androidx.compose.material3.Text
////import androidx.compose.material3.TopAppBar
////import androidx.compose.material3.TopAppBarDefaults
////import androidx.compose.runtime.Composable
////import androidx.compose.runtime.LaunchedEffect
////import androidx.compose.runtime.getValue
////import androidx.compose.runtime.mutableStateOf
////import androidx.compose.runtime.remember
////import androidx.compose.runtime.setValue
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.platform.LocalContext
////import androidx.compose.ui.res.stringResource
////import androidx.compose.ui.text.font.FontWeight
////import androidx.compose.ui.text.input.KeyboardType
////import androidx.compose.ui.tooling.preview.Preview
////import androidx.compose.ui.unit.dp
////import androidx.core.text.isDigitsOnly
////import androidx.navigation.NavHostController
////import com.quizle.R
////import com.quizle.presentation.common.LoadingButton
////import com.quizle.presentation.common.ToastMessageController
////import com.quizle.presentation.navigation.navigateToLogin
////import com.quizle.presentation.theme.DarkBackground
////import com.quizle.presentation.theme.GreenAccent
////import com.quizle.presentation.theme.SurfaceColor
////import com.quizle.presentation.util.LocaleHelper
////import kotlinx.coroutines.flow.Flow
////
////
////@Composable
////fun SettingsScreen(
////    toastManager: ToastMessageController,
////    appNavController: NavHostController,
////    state: SettingsState,
////    event: Flow<SettingsEvent>,
////    onAction: (SettingsAction) -> Unit
////) {
////
////    val context = LocalContext.current
////    SettingsContent(
////        state = state,
////        onLogoutClicked = { onAction(SettingsAction.LogoutButtonClicked) },
////        onLanguageSelected = { onAction(SettingsAction.LanguageButtonClicked(it))},
////        onSaveClicked = { onAction(SettingsAction.SaveButtonClicked)},
////        onQuizTimeEnabled = { onAction(SettingsAction.EnableQuizTimeButtonClicked)},
////        onCustomTimeEnabled = { onAction(SettingsAction.EnableCustomTimeButtonClicked)},
////        onCustomTimeInMinChanged = { onAction(SettingsAction.CustomTimeChanged(it))}
////    )
////
////
////    LaunchedEffect(key1 = Unit) {
////      event.collect {
////          when(it){
////              is SettingsEvent.ShowToast -> {
////                  val type = it.type
////                  val message = it.message
////                  toastManager.showToast(message,type)
////              }
////              is SettingsEvent.NavigateToSignUp -> {
////                  appNavController.navigateToLogin()
////              }
////
////
////              is SettingsEvent.ApplySettings -> {
////                  LocaleHelper.setLocale(context, it.language)
////                  (context as? Activity)?.recreate()
////              }
////          }
////      }
////    }
////
////
////
////}
////@SuppressLint("SuspiciousIndentation")
////@Composable
////fun SettingsContent(
////    state: SettingsState,
////    onLogoutClicked: () -> Unit,
////    onSaveClicked: () -> Unit,
////    onLanguageSelected: (String) -> Unit,
////    onQuizTimeEnabled: () -> Unit,
////    onCustomTimeEnabled:() -> Unit,
////    onCustomTimeInMinChanged:(Int) -> Unit
////
////) {
////    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
////
////    val langRes = if (state.selectedLanguage == "en") R.string.english else R.string.arabic
////
////        Column(
////            modifier = Modifier
////                .fillMaxSize()
////                .background(SurfaceColor)
////        ) {
////
////            TopAppBar(
////                windowInsets = WindowInsets(0.dp),
////                title = { Text(stringResource(R.string.settings)) },
////                colors = TopAppBarDefaults.topAppBarColors(
////                    containerColor = DarkBackground,
////                    titleContentColor = Color.White
////                )
////            )
////
////            Column(
////                modifier = Modifier
////                    .fillMaxSize()
////                    .padding(16.dp),
////                horizontalAlignment = Alignment.CenterHorizontally,
////                verticalArrangement = Arrangement.spacedBy(16.dp)
////            ) {
////                // Quiz Time Toggle
////                SettingsCard {
////                    Row(
////                        modifier = Modifier.fillMaxWidth(),
////                        horizontalArrangement = Arrangement.SpaceBetween,
////                        verticalAlignment = Alignment.CenterVertically
////                    ) {
////                        Text(text = stringResource(R.string.enable_quiz_time), fontWeight = FontWeight.Medium)
////                        Switch(
////                            colors = SwitchDefaults.colors(
////                                checkedThumbColor = DarkBackground,
////                                checkedTrackColor = GreenAccent
////                            ),
////                            checked = state.isQuizTimeEnabled,
////                            onCheckedChange = { onQuizTimeEnabled() }
////                        )
////                    }
////                }
////
////                // Custom Time Switch and Input Field
////                SettingsCard {
////                    Column(
////                        modifier = Modifier.fillMaxWidth(),
////                        horizontalAlignment = Alignment.Start
////                    ) {
////                        Row(
////                            modifier = Modifier.fillMaxWidth(),
////                            horizontalArrangement = Arrangement.SpaceBetween,
////                            verticalAlignment = Alignment.CenterVertically
////                        ) {
////                            Text(text = stringResource(R.string.use_custom_quiz_time), fontWeight = FontWeight.Medium)
////                            Switch(
////                                colors = SwitchDefaults.colors(
////                                    checkedThumbColor = DarkBackground,
////                                    checkedTrackColor = GreenAccent
////                                ),
////                                checked = state.isEnableCustomTimeSwitch,
////                                onCheckedChange = {onCustomTimeEnabled()}
////                            )
////                        }
////                        Spacer(modifier = Modifier.height(8.dp))
////                        OutlinedTextField(
////                            value = state.customTimeInMinutes.toString(),
////                            onValueChange = { newValue ->
////                                if (newValue.isNotEmpty() && newValue.isDigitsOnly()) {
////                                    onCustomTimeInMinChanged(newValue.toInt())
////                                } else if (newValue.isEmpty()) {
////                                    onCustomTimeInMinChanged(0)
////                                }
////                            },
////                            suffix = {
////                                Text(text = stringResource(R.string.min))
////                            },
////                            modifier = Modifier.fillMaxWidth(),
////                            singleLine = true,
////                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
////                            enabled = state.isEnableCustomTimeSwitch
////                        )
////                    }
////                }
////
////                // Language Dropdown List
////                SettingsCard {
////                    Column(modifier = Modifier.fillMaxWidth()) {
////                        Text(
////                            text = stringResource(R.string.language),
////                            style = MaterialTheme.typography.titleMedium,
////                            modifier = Modifier.padding(bottom = 8.dp)
////                        )
////                        Box(
////                            modifier = Modifier
////                                .fillMaxWidth()
////                                .wrapContentSize(Alignment.TopStart)
////                        ) {
////                            OutlinedButton(
////                                onClick = { isLanguageMenuExpanded = true },
////                                modifier = Modifier.fillMaxWidth()
////                            ) {
////                                Text(
////                                    text = stringResource(langRes),
////                                    modifier = Modifier.weight(1f),
////                                    color = DarkBackground
////                                )
////                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
////                            }
////                            DropdownMenu(
////                                expanded = isLanguageMenuExpanded,
////                                onDismissRequest = { isLanguageMenuExpanded = false }
////                            ) {
////                                DropdownMenuItem(
////                                    text = { Text(stringResource(R.string.english)) },
////                                    onClick = {
////                                        isLanguageMenuExpanded = false
////                                        onLanguageSelected("en")
////                                    }
////                                )
////                                DropdownMenuItem(
////                                    text = { Text(stringResource(R.string.arabic)) },
////                                    onClick = {
////                                        isLanguageMenuExpanded = false
////                                        onLanguageSelected("ar")
////                                    }
////                                )
////                            }
////                        }
////                    }
////
////            }
////
////                Spacer(modifier = Modifier.weight(1f))
////
////
////
////                LoadingButton(
////                    isLoading = state.isLoading,
////                    text = stringResource(R.string.save),
////                    onClick = onSaveClicked,
////                    color = GreenAccent,
////                    textColor = DarkBackground,
////                    fontWeight = FontWeight.Bold
////                )
////
////                // Logout Button
////                Button(
////                    onClick = onLogoutClicked,
////                    modifier = Modifier.fillMaxWidth(),
////                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
////                ) {
////                    Text(text = stringResource(R.string.logout), color = MaterialTheme.colorScheme.onError)
////                }
////                Spacer(modifier = Modifier.height(16.dp))
////            }
////
////
////        }
////
////}
////
////@Composable
////fun SettingsCard(
////    content: @Composable () -> Unit) {
////    Card(
////        modifier = Modifier
////            .fillMaxWidth(),
////        shape = RoundedCornerShape(12.dp),
////        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
////        colors = CardDefaults.cardColors(containerColor = Color.White)
////    ) {
////        Box(modifier = Modifier.padding(16.dp)) {
////            content()
////        }
////    }
////}
////
////@Preview(showBackground = true)
////@Composable
////private fun SettingsPreview() {
////    val state = SettingsState()
////    SettingsContent(
////        onLogoutClicked = {},
////        onSaveClicked = {},
////        onLanguageSelected = {},
////        state = state,
////        onQuizTimeEnabled = {},
////        onCustomTimeEnabled = {  },
////        onCustomTimeInMinChanged = {},
////    )
////}
