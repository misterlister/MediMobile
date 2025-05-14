package com.example.medimobile.ui.components.templates

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorPopup(
    errorTitle: String = "Error",
    errorMessage: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = errorTitle) },
        text = { Text(errorMessage) },
        confirmButton = {
            MediButton(onClick = onDismiss) {
                Text("Ok")
            }
        }
    )
}
