package com.quizle.presentation.common


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    text: String,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Search,
    onTextChange: (String) -> Unit = {},
    colorContainer: Color = MaterialTheme.extendedColors.onSurfaceColor,
    colorContent: Color = MaterialTheme.extendedColors.primaryColor,
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
                color = colorContent.copy(alpha = 0.5f) // Correct placeholder color
            )
        },
        leadingIcon = {
            IconButton(onClick = { onSearchClick(text) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = colorContent
                )
            }
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = { onTextChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Icon",
                        tint = colorContent
                    )
                }
            }
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(onSearch = { onSearchClick(text) }),
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = colorContent,
            unfocusedIndicatorColor = colorContent,
            disabledIndicatorColor = colorContent,
            focusedContainerColor = colorContainer,
            unfocusedContainerColor = colorContainer,
            focusedTextColor = colorContent,
            unfocusedTextColor = colorContent,
        )
    )
}



@Preview(name = "Search Bar Empty - Light")
@Composable
private fun SearchBarEmptyLightPreview() {
    QuizleTheme(darkTheme = true) {

        Box(modifier = Modifier.padding(16.dp)) {
            SearchBar(
                hint = "Search topics...",
                onTextChange = { },
                text = ""
            )
        }
    }
}

@Preview(name = "Search Bar With Text - Dark")
@Composable
private fun SearchBarWithTextDarkPreview() {
    QuizleTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            SearchBar(
                hint = "Search topics...",
                text = "Android",
                onTextChange = { }
            )
        }
    }
}