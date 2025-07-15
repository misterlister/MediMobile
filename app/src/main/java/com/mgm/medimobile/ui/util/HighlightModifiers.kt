package com.mgm.medimobile.ui.util

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.highlightIf(condition: Boolean): Modifier {
    return if (condition) {
        this
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(4.dp)
            )
    } else {
        this
    }
}