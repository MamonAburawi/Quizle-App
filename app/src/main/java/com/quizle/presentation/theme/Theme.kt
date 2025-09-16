package com.quizle.presentation.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.quizle.presentation.theme.Color.SemanticRed
import androidx.compose.ui.platform.LocalContext
import com.quizle.presentation.theme.Color.BackGroundDark
import com.quizle.presentation.theme.Color.BackGroundLight
import com.quizle.presentation.theme.Color.PrimaryDark
import com.quizle.presentation.theme.Color.PrimaryLight
import com.quizle.presentation.theme.Color.SecondaryDark
import com.quizle.presentation.theme.Color.SecondaryLight
import com.quizle.presentation.theme.Color.SurfaceDark
import com.quizle.presentation.theme.Color.SurfaceLight
import com.quizle.presentation.theme.Color.TextPrimaryDark
import com.quizle.presentation.theme.Color.TextPrimaryLight
import com.quizle.presentation.theme.Color.TextSecondaryDark
import com.quizle.presentation.theme.Color.TextSecondaryLight
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.CompositionLocalProvider

@Immutable
data class ExtendedColorScheme(
    val textPrimaryColor: Color,
    val textSecondaryColor: Color,
    val backgroundColor: Color,
    val surfaceColor: Color,
    val onSurfaceColor: Color,
    val onBackgroundColor: Color,
    val primaryColor: Color,
    val secondaryColor: Color,
    val error: Color
)


// Light Palette
val lightExtendedColors = ExtendedColorScheme(
    textPrimaryColor = TextPrimaryLight,
    textSecondaryColor = TextSecondaryLight,
    backgroundColor = BackGroundLight,
    surfaceColor = SurfaceLight,
    primaryColor = PrimaryLight,
    secondaryColor = SecondaryLight,
    onSurfaceColor = TextPrimaryLight,
    onBackgroundColor = TextPrimaryLight,
    error = SemanticRed
)

// Dark Palette
val darkExtendedColors = ExtendedColorScheme(
    textPrimaryColor = TextPrimaryDark,
    textSecondaryColor = TextSecondaryDark,
    backgroundColor = BackGroundDark,
    surfaceColor = SurfaceDark,
    primaryColor = PrimaryDark,
    secondaryColor = SecondaryDark,
    onSurfaceColor = TextPrimaryDark,
    onBackgroundColor = TextPrimaryDark,
    error = SemanticRed
)



private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    background = BackGroundLight,
    surface = SurfaceLight,
    onPrimary = TextPrimaryDark,
    onSecondary = TextPrimaryDark,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    background = BackGroundDark,
    surface = SurfaceDark,
    onPrimary = TextPrimaryDark,
    onSecondary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)


private val LocalExtendedColors = staticCompositionLocalOf { lightExtendedColors }

val MaterialTheme.extendedColors: ExtendedColorScheme
    @Composable
    get() = LocalExtendedColors.current


@Composable
fun QuizleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {


    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Step B: Select your CUSTOM ExtendedColorScheme
    val extendedColors = if (darkTheme) darkExtendedColors else lightExtendedColors

    // Step C: Provide the custom colors and apply the standard theme
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography, // Your typography
            content = content
        )
    }
}
