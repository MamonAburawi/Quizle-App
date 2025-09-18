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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.QuizleTheme
import com.quizle.presentation.theme.extendedColors


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    text: String,
    isLoading: Boolean = true,
    onTextChange: (String) -> Unit = {},
    colorContainer: Color = MaterialTheme.extendedColors.onSurface,
    colorContent: Color = MaterialTheme.extendedColors.primary,
    onSearchClick: (String) -> Unit = {},
) {
    var isEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(isLoading) {
        isEnabled = !isLoading
    }

    TextFieldBox(
        modifier = modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = onTextChange,
        enabled = isEnabled,
        hint = hint,
        shape = RoundedCornerShape(50.dp),
        hintColor = colorContent.copy(alpha = 0.5f),
        textColor = colorContent,
        cursorColor = colorContent,
        focusContainerColor = colorContainer,
        unFocusContainerColor = colorContainer,
        keyboardType = KeyboardType.Text,
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
        imeAction = ImeAction.Search,
        keyboardActions = KeyboardActions(onSearch = { onSearchClick(text) }),
        singleLine = true,
        minLines = 1
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

