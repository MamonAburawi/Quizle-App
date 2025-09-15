package com.quizle.presentation.theme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//val primaryLight = Color(0xFF6D5E0F)
//val onPrimaryLight = Color(0xFFFFFFFF)
//val primaryContainerLight = Color(0xFFF8E287)
//val onPrimaryContainerLight = Color(0xFF534600)
//val secondaryLight = Color(0xFF665E40)
//val onSecondaryLight = Color(0xFFFFFFFF)
//val secondaryContainerLight = Color(0xFFEEE2BC)
//val onSecondaryContainerLight = Color(0xFF4E472A)
//val tertiaryLight = Color(0xFF43664E)
//val onTertiaryLight = Color(0xFFFFFFFF)
//val tertiaryContainerLight = Color(0xFFC5ECCE)
//val onTertiaryContainerLight = Color(0xFF2C4E38)
//val errorLight = Color(0xFFBA1A1A)
//val onErrorLight = Color(0xFFFFFFFF)
//val errorContainerLight = Color(0xFFFFDAD6)
//val onErrorContainerLight = Color(0xFF93000A)
//val backgroundLight = Color(0xFF28324E)
//val onBackgroundLight = Color(0xFF1E1B13)
//val surfaceLight = Color(0xFF28324E)
//val onSurfaceLight = Color(0xFF1E1B13)
//val surfaceVariantLight = Color(0xFFEAE2D0)
//val onSurfaceVariantLight = Color(0xFF4B4739)
//val outlineLight = Color(0xFF7C7767)
//val outlineVariantLight = Color(0xFFCDC6B4)
//val scrimLight = Color(0xFF000000)
//val inverseSurfaceLight = Color(0xFF333027)
//val inverseOnSurfaceLight = Color(0xFFF7F0E2)
//val inversePrimaryLight = Color(0xFFDBC66E)
//val surfaceDimLight = Color(0xFFE0D9CC)
//val surfaceBrightLight = Color(0xFFFFF9EE)
//val surfaceContainerLowestLight = Color(0xFFFFFFFF)
//val surfaceContainerLowLight = Color(0xFFFAF3E5)
//val surfaceContainerLight = Color(0xFFF4EDDF)
//val surfaceContainerHighLight = Color(0xFF28324E)
//val surfaceContainerHighestLight = Color(0xFFE8E2D4)
//
//
//
//val primaryDark = Color(0xFFDBC66E)
//val onPrimaryDark = Color(0xFF3A3000)
//val primaryContainerDark = Color(0xFF534600)
//val onPrimaryContainerDark = Color(0xFFF8E287)
//val secondaryDark = Color(0xFFD1C6A1)
//val onSecondaryDark = Color(0xFF363016)
//val secondaryContainerDark = Color(0xFF4E472A)
//val onSecondaryContainerDark = Color(0xFFEEE2BC)
//val tertiaryDark = Color(0xFFA9D0B3)
//val onTertiaryDark = Color(0xFF143723)
//val tertiaryContainerDark = Color(0xFF2C4E38)
//val onTertiaryContainerDark = Color(0xFFC5ECCE)
//val errorDark = Color(0xFFFFB4AB)
//val onErrorDark = Color(0xFF690005)
//val errorContainerDark = Color(0xFF93000A)
//val onErrorContainerDark = Color(0xFFFFDAD6)
//val backgroundDark = Color(0xFF15130B)
//val onBackgroundDark = Color(0xFFE8E2D4)
//val surfaceDark = Color(0xFF15130B)
//val onSurfaceDark = Color(0xFFE8E2D4)
//val surfaceVariantDark = Color(0xFF4B4739)
//val onSurfaceVariantDark = Color(0xFFCDC6B4)
//val outlineDark = Color(0xFF969080)
//val outlineVariantDark = Color(0xFF4B4739)
//val scrimDark = Color(0xFF000000)
//val inverseSurfaceDark = Color(0xFFE8E2D4)
//val inverseOnSurfaceDark = Color(0xFF333027)
//val inversePrimaryDark = Color(0xFF6D5E0F)
//val surfaceDimDark = Color(0xFF15130B)
//val surfaceBrightDark = Color(0xFF3C3930)
//val surfaceContainerLowestDark = Color(0xFF100E07)
//val surfaceContainerLowDark = Color(0xFF1E1B13)
//val surfaceContainerDark = Color(0xFF222017)
//val surfaceContainerHighDark = Color(0xFF2D2A21)
//val surfaceContainerHighestDark = Color(0xFF38352B)


// Brand Colors (used in both themes)
val PurplePrimary = Color(0xFF6F42C1)
val PurpleVariant = Color(0xFF4C2A8A)

// Dark Theme specific colors
val DarkBackground = Color(0xFF1E2741)
val DarkSurface = Color(0xFF28324E)
val DarkOnBackground = Color.White
val DarkOnSurface = Color.White
val DarkSecondaryAccent = Color(0xFF00FFC0) // Bright Green

// Light Theme specific colors
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFFFFFF)
val LightOnBackground = Color(0xFF1A1A1A)
val LightOnSurface = Color(0xFF1A1A1A)
val LightSecondaryAccent = Color(0xFF00897B) // Muted Teal for better contrast

// Semantic Colors
val SemanticRed = Color(0xFFF44336)
val SemanticGreen = Color(0xFF4CAF50)
val SemanticOrange = Color(0xFFFFA726)

val SemanticBlue = Color(0xFF2196F3)




// Theme-aware extension properties for custom semantic colors
val Color.Companion.success: Color
    @Composable
    get() = SemanticGreen

val Color.Companion.warning: Color
    @Composable
    get() = SemanticOrange

val Color.Companion.error: Color
    @Composable
    get() = SemanticRed

val Color.Companion.info: Color
    @Composable
    get() = SemanticBlue










//val CustomGreen = Color(0xFF1B5E20)
//
//val DarkGradientStart = Color(0xFF2C3E50)
//val DarkGradientEnd = Color(0xFF1B2631)
//val MutedGreen = Color(0xFF2ECC71)
//val MutedRed = Color(0xFFE74C3C)
//val TextFieldColorPrimary = Color.White
//val TextSecondary = Color.Gray
//val ComponentBackground = Color(0xFF34495E)
//
//// Define colors used in the design
//val PrimaryColor = Color(0xFF1E2741)
//val SurfaceColor = Color(0xFF28324E)
//
//
//// Define colors used in the design
//val DarkBackground = Color(0xFF1E2741)
//val colorSurface = Color(0xFF28324E)
//val LightPurple = Color(0xFF6F42C1)
//val DarkPurple = Color(0xFF4C2A8A)
//val GreenAccent = Color(0xFF00FFC0)
//val GrayText = Color(0xFFA0A0A0)
//val CardBackground = Color(0xFF28324E)
//val Blue = Color(0xFF3A86FF)
//val Gray = Color(0xFFB0B0B0)
//
//val Green = Color(0xFF4CAF50)
//val Yellow = Color(0xFFFFEB3B)
//val Red = Color(0xFFF44336)







