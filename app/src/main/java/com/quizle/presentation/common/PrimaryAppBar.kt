@file:OptIn(ExperimentalMaterial3Api::class)

package com.quizle.presentation.common


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.quizle.presentation.theme.QuizleTheme



@Composable
fun PrimaryAppBar(
    title: String,
    onBack: () -> Unit,
    // NEW: Defaults are now from the MaterialTheme color scheme
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize
) {
    TopAppBar(
        windowInsets = WindowInsets(0),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor
        ),
        title = {
            Text(
                text = title,
                fontSize = fontSize
                // No need to specify color here, it's inherited from titleContentColor
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Icon"
                )
            }
        }
    )
}




@Preview(name = "Primary App Bar - Light Theme", showBackground = true)
@Composable
private fun PrimaryAppBarLightPreview() {
    QuizleTheme(darkTheme = false) {
        PrimaryAppBar(
            title = "Settings",
            onBack = {}
        )
    }
}



@Preview(name = "Primary App Bar - Dark", showBackground = true)
@Composable
private fun PrimaryAppBarDarkPreview() {
    QuizleTheme(darkTheme = true) {
        PrimaryAppBar(
            title = "Profile",
            onBack = {},
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}