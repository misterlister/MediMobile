package com.example.medimobile.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// Custom TextStyle for app title text
val appTitleTextStyle = TextStyle(
    fontWeight = FontWeight.Bold, // Bold text
    fontSize = 44.sp, // Font size
    textAlign = TextAlign.Center // Center text alignment
)

// Custom TextStyle for section title text
val sectionTitleTextStyle = TextStyle(
    fontWeight = FontWeight.Bold, // Bold text
    fontSize = 24.sp, // Font size
    textAlign = TextAlign.Center // Center text alignment
)

// Custom TextStyle for screen title text
val screenTitleTextStyle = TextStyle(
    fontWeight = FontWeight.Bold, // Bold text
    fontSize = 36.sp, // Font size
    textAlign = TextAlign.Center // Center text alignment
)

// Custom TextStyle for username and DocID text
val userNameTextStyle = TextStyle(
    fontWeight = FontWeight.Bold, // Bold text
    fontSize = 18.sp, // Font size
)

val errorMessageTextStyle = TextStyle(
    fontWeight = FontWeight.Bold, // Bold text
    fontSize = 18.sp, // Font size
    color = Color.Red // Set text color to red for error messages
)

@Composable
fun MediMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}