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


@Composable
fun PrimaryAppBar(
    title: String,
    onBack:() -> Unit,
    contentColor: Color = Color.Black,
    containerColor :Color = Color.White,
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
                fontSize = fontSize,
                color = contentColor
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Icon"
                )
            }
        }
    )
}


@Preview
@Composable
private fun PrimaryAppBarPreview() {
    PrimaryAppBar(
        title = "Report an issue",
        onBack = {}
    )
}