package com.quizle.presentation.common


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import com.quizle.R
import com.quizle.presentation.theme.extendedColors
import kotlin.math.round


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: ()-> Unit,
    text: String,
    loadingColor: Color = MaterialTheme.extendedColors.primary,
    bgColor: Color = MaterialTheme.extendedColors.primary,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    fontWeight: FontWeight = FontWeight.Normal,
    isLoading: Boolean = false,
    contentColor: Color = MaterialTheme.extendedColors.textPrimary
) {

    var buttonColor by mutableStateOf(bgColor)
    var isEnabled by mutableStateOf(true)

    LaunchedEffect(isLoading) {
        buttonColor = if (isLoading) loadingColor else bgColor
        isEnabled = !isLoading
    }

    val corner = RoundedCornerShape(16.dp)
    Button(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 26.dp, ambientColor = Color.White.copy(alpha = 0.8f), shape = corner)
            .height(45.dp),
        shape = corner,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            disabledContainerColor = buttonColor
        ),
        enabled = isEnabled,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(targetState = isLoading, label = "Primary Button") { showLoader ->
                if (showLoader) {
                    CircularProgressIndicator(
                        color = contentColor,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
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
}


@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    outLineColor: Color = MaterialTheme.extendedColors.primary,
    contentColor: Color = MaterialTheme.extendedColors.primary,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    isLoading: Boolean = false,
    text: String,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight = FontWeight.Normal

    ) {
    var isEnabled by mutableStateOf(true)

    LaunchedEffect(isLoading) {
        isEnabled = !isLoading
    }

    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor),
        border = BorderStroke(1.dp, outLineColor),
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(targetState = isLoading, label = "ContentSwitcher") { showLoader ->
                if (showLoader) {
                    CircularProgressIndicator(
                        color = contentColor,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Text(
                        text = text,
                        fontWeight = fontWeight,
                        fontStyle = fontStyle,
                        fontSize = fontSize,
                    )
                }
            }
        }
    }
}



@Preview
@Composable
private fun PrimaryButtonPreview() {
    PrimaryButton(
        onClick = {},
        text = "Login",
        fontWeight = FontWeight.Bold,
        isLoading = false,
    )
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    SecondaryButton(
        onClick = {},
        text = "Logout",
        isLoading = false,
        outLineColor = Color.Red,
        contentColor = Color.Red
    )
}