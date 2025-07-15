package com.mgm.medimobile.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSectionDivider() {
    HorizontalDivider(thickness = 1.dp, color = Color.Black)
}

@Composable
fun VerticalSectionDivider() {
    VerticalDivider(thickness = 1.dp, color = Color.Black)
}
