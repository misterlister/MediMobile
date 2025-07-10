package com.example.medimobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColorScheme(
    val medigreen: ColorFamily,
    val mediyellow: ColorFamily,
    val medigrey: ColorFamily
)

val LocalExtendedColors = staticCompositionLocalOf<ExtendedColorScheme> {
    error("No ExtendedColorScheme provided")
}

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

val extendedLight = ExtendedColorScheme(
    medigreen = ColorFamily(
        medigreenLight,
        onMedigreenLight,
        medigreenContainerLight,
        onMedigreenContainerLight,
    ),
    mediyellow = ColorFamily(
        mediyellowLight,
        onMediyellowLight,
        mediyellowContainerLight,
        onMediyellowContainerLight,
    ),
    medigrey = ColorFamily(
        medigreyLight,
        onMedigreyLight,
        medigreyContainerLight,
        onMedigreyContainerLight,
    )
)

val extendedDark = ExtendedColorScheme(
    medigreen = ColorFamily(
        medigreenDark,
        onMedigreenDark,
        medigreenContainerDark,
        onMedigreenContainerDark,
    ),
    mediyellow = ColorFamily(
        mediyellowDark,
        onMediyellowDark,
        mediyellowContainerDark,
        onMediyellowContainerDark,
    ),
    medigrey = ColorFamily(
        medigreyDark,
        onMedigreyDark,
        medigreyContainerDark,
        onMedigreyContainerDark,
    )
)

val extendedLightMediumContrast = ExtendedColorScheme(
    medigreen = ColorFamily(
        medigreenLightMediumContrast,
        onMedigreenLightMediumContrast,
        medigreenContainerLightMediumContrast,
        onMedigreenContainerLightMediumContrast,
    ),
    mediyellow = ColorFamily(
        mediyellowLightMediumContrast,
        onMediyellowLightMediumContrast,
        mediyellowContainerLightMediumContrast,
        onMediyellowContainerLightMediumContrast,
    ),
    medigrey = ColorFamily(
        medigreyLightMediumContrast,
        onMedigreyLightMediumContrast,
        medigreyContainerLightMediumContrast,
        onMedigreyContainerLightMediumContrast,
    )
)

val extendedLightHighContrast = ExtendedColorScheme(
    medigreen = ColorFamily(
        medigreenLightHighContrast,
        onMedigreenLightHighContrast,
        medigreenContainerLightHighContrast,
        onMedigreenContainerLightHighContrast,
    ),
    mediyellow = ColorFamily(
        mediyellowLightHighContrast,
        onMediyellowLightHighContrast,
        mediyellowContainerLightHighContrast,
        onMediyellowContainerLightHighContrast,
    ),
    medigrey = ColorFamily(
        medigreyLightHighContrast,
        onMedigreyLightHighContrast,
        medigreyContainerLightHighContrast,
        onMedigreyContainerLightHighContrast,
    )
)

val extendedDarkMediumContrast = ExtendedColorScheme(
    medigreen = ColorFamily(
        medigreenDarkMediumContrast,
        onMedigreenDarkMediumContrast,
        medigreenContainerDarkMediumContrast,
        onMedigreenContainerDarkMediumContrast,
    ),
    mediyellow = ColorFamily(
        mediyellowDarkMediumContrast,
        onMediyellowDarkMediumContrast,
        mediyellowContainerDarkMediumContrast,
        onMediyellowContainerDarkMediumContrast,
    ),
    medigrey = ColorFamily(
        medigreyDarkMediumContrast,
        onMedigreyDarkMediumContrast,
        medigreyContainerDarkMediumContrast,
        onMedigreyContainerDarkMediumContrast,
    )
)

val extendedDarkHighContrast = ExtendedColorScheme(
    medigreen = ColorFamily(
        medigreenDarkHighContrast,
        onMedigreenDarkHighContrast,
        medigreenContainerDarkHighContrast,
        onMedigreenContainerDarkHighContrast,
    ),
    mediyellow = ColorFamily(
        mediyellowDarkHighContrast,
        onMediyellowDarkHighContrast,
        mediyellowContainerDarkHighContrast,
        onMediyellowContainerDarkHighContrast,
    ),
    medigrey = ColorFamily(
        medigreyDarkHighContrast,
        onMedigreyDarkHighContrast,
        medigreyContainerDarkHighContrast,
        onMedigreyContainerDarkHighContrast,
    )
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

enum class BrightnessMode{
    SYSTEM, LIGHT, DARK
}

enum class ContrastLevel{
    LOW, MEDIUM, HIGH
}

@Composable
fun MediMobileTheme(
    brightnessMode: BrightnessMode = BrightnessMode.SYSTEM,
    contrastLevel: ContrastLevel = ContrastLevel.MEDIUM,
    content: @Composable () -> Unit
) {
    val isDarkTheme = when (brightnessMode) {
        BrightnessMode.SYSTEM -> isSystemInDarkTheme()
        BrightnessMode.LIGHT -> false
        BrightnessMode.DARK -> true
    }

    val colorScheme = when (contrastLevel) {
        ContrastLevel.LOW -> if (isDarkTheme) darkScheme else lightScheme
        ContrastLevel.MEDIUM -> if (isDarkTheme) mediumContrastDarkColorScheme else mediumContrastLightColorScheme
        ContrastLevel.HIGH -> if (isDarkTheme) highContrastDarkColorScheme else highContrastLightColorScheme
    }

    val extendedColors = when (contrastLevel) {
        ContrastLevel.LOW -> if (isDarkTheme) extendedDark else extendedLight
        ContrastLevel.MEDIUM -> if (isDarkTheme) extendedDarkMediumContrast else extendedLightMediumContrast
        ContrastLevel.HIGH -> if (isDarkTheme) extendedDarkHighContrast else extendedLightHighContrast
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

enum class ButtonStatus {
    DEFAULT, CONFIRM, WARNING, SELECTED;

    @Composable
    fun backgroundColor(): Color = when (this) {
        DEFAULT -> MaterialTheme.colorScheme.primaryContainer
        CONFIRM -> LocalExtendedColors.current.medigreen.colorContainer
        WARNING -> MaterialTheme.colorScheme.errorContainer
        SELECTED -> MaterialTheme.colorScheme.primary
    }

    @Composable
    fun contentColor(): Color = when (this) {
        DEFAULT -> MaterialTheme.colorScheme.onPrimaryContainer
        CONFIRM -> LocalExtendedColors.current.medigreen.onColorContainer
        WARNING -> MaterialTheme.colorScheme.onErrorContainer
        SELECTED -> MaterialTheme.colorScheme.onPrimary
    }
}