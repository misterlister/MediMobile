package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.theme.ButtonStatus

@Composable
fun MediButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    status: ButtonStatus = ButtonStatus.DEFAULT,
    emptyHighlight: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val shape = RoundedCornerShape(4.dp)

    Button(
        onClick = onClick,
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = status.backgroundColor(),
            contentColor = status.contentColor()
        ),
        shape = shape,
        border = if (emptyHighlight) BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary) else null,
        content = content,
        modifier = modifier
    )
}