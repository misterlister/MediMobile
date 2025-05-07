package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.medimobile.ui.theme.ButtonStatus

@Composable
fun MediButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    status: ButtonStatus = ButtonStatus.DEFAULT,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = status.backgroundColor,
            contentColor = status.contentColor
        ),
        content = content
    )
}