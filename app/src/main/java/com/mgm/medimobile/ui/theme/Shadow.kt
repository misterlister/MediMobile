package com.mgm.medimobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow

// Get corresponding shadow color for light/dark mode
@Composable
fun dynamicShadowColor(): Color {
    return if (isSystemInDarkTheme()) Color.White else Color.Black
}

@Composable
fun lightShadow(): Shadow = Shadow(
    color = dynamicShadowColor().copy(alpha = 0.2f),
    offset = Offset(2f, 2f),
    blurRadius = 3f
)

@Composable
fun mediumShadow(): Shadow = Shadow(
    color = dynamicShadowColor().copy(alpha = 0.3f),
    offset = Offset(3f, 3f),
    blurRadius = 5f
)

@Composable
fun heavyShadow(): Shadow = Shadow(
    color = dynamicShadowColor().copy(alpha = 0.4f),
    offset = Offset(4f, 4f),
    blurRadius = 7f
)