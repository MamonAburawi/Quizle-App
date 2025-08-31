package com.quizle.presentation.common


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.DarkBackground
import com.quizle.presentation.theme.PrimaryColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    text : String = "",
    enabled: Boolean = true,
    contentColor: Color = PrimaryColor,
    containerColor: Color = Color.White,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Search,
    onSearchClick: (String) -> Unit = {},
    onTextChange:(String) -> Unit
) {
    var v by remember{ mutableStateOf(text) }


    TextField(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .height(55.dp)
            .focusable(false),
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = contentColor
        ),
        enabled = enabled,
        value = v,
        placeholder = {
            Text(
                text = hint,
                color =  contentColor,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
        },

        onValueChange = {
                v = it
                onTextChange(v)
        },
        leadingIcon = {
            Row(
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                IconButton(
                    onClick = {
                        onSearchClick(v)
                    }
                ) {
                    Image(
                        modifier = Modifier
                            .size(22.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "search icon",
                        colorFilter = ColorFilter.tint(contentColor)
                    )
                }
            }

        },
        trailingIcon = {
            if (v.isNotEmpty()){
                Image(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { v = "" }
                        .size(22.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "clear icon",
                    colorFilter = ColorFilter.tint(contentColor)
                )
                Spacer(modifier = Modifier.width(15.dp))
            }
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClick(v)
            }
        ),
        colors = TextFieldDefaults.colors(
            cursorColor = contentColor,
            focusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = contentColor,
            unfocusedIndicatorColor = contentColor,
            focusedTextColor = contentColor,
            unfocusedTextColor = contentColor
        )
    )

}

@Preview
@Composable
private fun SearchBarPreview() {
    SearchBar(
        hint = "Search",
        onTextChange = {}
    )
}

