package com.example.medimobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = MediBlue,
    onPrimary = TextOnBlue,
    background = DarkBackground,
    onBackground = TextOnDark,
    surface = DarkBackground,
    onSurface = TextOnDark,
    outline = DarkVariant,
    surfaceVariant = DarkVariant,
    onSurfaceVariant = TextOnDark,
    primaryContainer = DarkContainer,
    onPrimaryContainer = TextOnDark,
    secondaryContainer = SecondaryDarkContainer,
    onSecondaryContainer = TextOnDark,
)

private val LightColorScheme = lightColorScheme(
    primary = MediBlue,
    onPrimary = TextOnBlue,
    background = LightBackground,
    onBackground = TextOnLight,
    surface = LightBackground,
    onSurface = TextOnLight,
    outline = LightVariant,
    surfaceVariant = LightVariant,
    onSurfaceVariant = TextOnLight,
    primaryContainer = LightContainer,
    onPrimaryContainer = TextOnLight,
    secondaryContainer = SecondaryLightContainer,
    onSecondaryContainer = TextOnLight,
)

// Custom TextStyle for app title text
val appTitleTextStyle = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 44.sp,
    textAlign = TextAlign.Center,
    shadow = Shadow(
        color = Color.Black.copy(alpha = 0.5f),
        offset = Offset(4f, 4f),
        blurRadius = 7f
    )
)

// Custom TextStyle for section title text
val sectionTitleTextStyle = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    textAlign = TextAlign.Center,
    shadow = Shadow(
        color = Color.Black.copy(alpha = 0.3f),
        offset = Offset(2f, 2f),
        blurRadius = 5f
    )
)

// Custom TextStyle for screen title text
val screenTitleTextStyle = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 36.sp,
    textAlign = TextAlign.Center,
    shadow = Shadow(
        color = Color.Black.copy(alpha = 0.4f),
        offset = Offset(3f, 3f),
        blurRadius = 6f
    )
)

// Custom TextStyle for username and DocID text
val userNameTextStyle = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
)

val errorMessageTextStyle = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    color = Color.Red // Set text color to red for error messages
)

val placeholderTextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    color = Color.Gray // Set text color to gray for placeholders
)

@Composable
fun MediMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Get corresponding banner colour for light/dark mode
@Composable
fun bannerColor(): Color {
    return if (isSystemInDarkTheme()) DarkBanner else LightBanner
}

enum class ButtonStatus(val backgroundColor: Color, val contentColor: Color) {
    DEFAULT(MediBlue, TextOnBlue),
    CONFIRM(MediGreen, TextOnGreen),
    WARNING(MediRed, TextOnRed),
    DISABLED(MediGrey, TextOnGrey)
}