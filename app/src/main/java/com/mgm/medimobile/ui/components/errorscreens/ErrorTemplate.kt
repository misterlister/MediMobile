package com.mgm.medimobile.ui.components.errorscreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorTemplate(
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            errorMessage,
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.error),
            modifier = modifier
                .align(Alignment.Center)
        )
    }
}