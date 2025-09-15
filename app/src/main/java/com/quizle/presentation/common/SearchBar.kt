package com.quizle.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.QuizleTheme


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Search,
    onTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit = {},
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp) // Standard height for text fields
            .clip(RoundedCornerShape(50)), // Fully rounded corners
        value = text,
        onValueChange = onTextChange,
        enabled = enabled,
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Correct placeholder color
            )
        },
        leadingIcon = {
            IconButton(onClick = { onSearchClick(text) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = { onTextChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Icon"
                    )
                }
            }
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(onSearch = { onSearchClick(text) }),
        shape = RoundedCornerShape(50),
        // NEW: Simplified, theme-aware colors
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

// --- PREVIEWS ---

@Preview(name = "Search Bar Empty - Light", showBackground = true)
@Composable
private fun SearchBarEmptyLightPreview() {
    QuizleTheme(darkTheme = true) {
        var text by remember { mutableStateOf("") }
        Box(modifier = Modifier.padding(16.dp)) {
            SearchBar(
                text = text,
                hint = "Search topics...",
                onTextChange = { text = it }
            )
        }
    }
}

@Preview(name = "Search Bar With Text - Dark", showBackground = true)
@Composable
private fun SearchBarWithTextDarkPreview() {
    QuizleTheme(darkTheme = true) {
        var text by remember { mutableStateOf("History") }
        Box(modifier = Modifier.padding(16.dp)) {
            SearchBar(
                text = text,
                hint = "Search topics...",
                onTextChange = { text = it }
            )
        }
    }
}