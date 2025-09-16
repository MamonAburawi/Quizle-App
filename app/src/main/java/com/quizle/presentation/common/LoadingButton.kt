package com.quizle.presentation.common


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.quizle.presentation.theme.extendedColors


@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    onClick: ()-> Unit,
    text: String,
    loadingColor: Color = MaterialTheme.extendedColors.primaryColor,
    bgColor: Color = MaterialTheme.extendedColors.primaryColor,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    fontWeight: FontWeight = FontWeight.Normal,
    isLoading: Boolean = false,
    contentColor: Color = MaterialTheme.extendedColors.textPrimaryColor
) {
    var buttonColor by mutableStateOf(bgColor)

    LaunchedEffect(isLoading) {
         buttonColor = if (isLoading) loadingColor else bgColor
    }

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            disabledContainerColor = buttonColor
        ),
        enabled = !isLoading,
        onClick = {
            onClick()
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading){
                CircularProgressIndicator(
                    color = contentColor,
                    modifier = Modifier.size(30.dp)
                )
            }else {
                Text(
                    text = text,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    color = contentColor
                )
            }
        }
    }
}



@Preview
@Composable
private fun LoadingButtonPreview() {
    LoadingButton(
        onClick = {},
        text = "Login",
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        fontWeight = FontWeight.Bold,
        isLoading = false,
    )
}