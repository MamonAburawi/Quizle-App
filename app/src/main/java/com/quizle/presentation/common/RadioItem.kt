package com.quizle.presentation.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.quizle.presentation.theme.QuizleTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quizle.presentation.theme.extendedColors
import com.quizle.presentation.theme.unSelected


@Composable
fun RadioItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onSelectedItem: () -> Unit,
    option: String,
    selectedColor: Color = MaterialTheme.extendedColors.onSurface,
    unSelectedColor: Color = Color.unSelected,


    ) {
    val colors = RadioButtonDefaults.colors(
        selectedColor = selectedColor,
        unselectedColor = unSelectedColor,
    )

    Row(
        modifier = modifier
            .selectable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                selected = isSelected,
                onClick = onSelectedItem,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = colors
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = option,
            style = MaterialTheme.typography.bodyLarge,
            // NEW: Color is no longer hardcoded, it uses the theme's default onSurface color
        )
    }
}


@Preview(name = "Radio Group - Light Theme", showBackground = true)
@Composable
private fun RadioItemLightPreview() {
    QuizleTheme(darkTheme  = false) {
        var selectedOption by remember { mutableStateOf("Option 2") }
        val options = listOf("Option 1", "Option 2", "Option 3")

        Column(modifier = Modifier.padding(16.dp)) {
            options.forEach { option ->
                RadioItem(
                    selectedColor = MaterialTheme.extendedColors.primary,
                    isSelected = (selectedOption == option),
                    onSelectedItem = { selectedOption = option },
                    option = option
                )
            }
        }
    }
}

@Preview(name = "Radio Group - Dark Theme", showBackground = true)
@Composable
private fun RadioItemDarkPreview() {
    QuizleTheme(darkTheme = true) {
        var selectedOption by remember { mutableStateOf("Option 2") }
        val options = listOf("Option 1", "Option 2", "Option 3")

        Column(modifier = Modifier.padding(16.dp)) {
            options.forEach { option ->
                RadioItem(
                    selectedColor = MaterialTheme.extendedColors.primary,
                    isSelected = (selectedOption == option),
                    onSelectedItem = { selectedOption = option },
                    option = option
                )
            }
        }
    }
}