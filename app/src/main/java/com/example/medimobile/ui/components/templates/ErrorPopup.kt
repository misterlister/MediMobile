package com.example.medimobile.ui.components.templates

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun ErrorPopup(
    modifier: Modifier = Modifier,
    errorTitle: String = "Error",
    errorMessage: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = errorTitle) },
        text = { Text(
            text = errorMessage,
            modifier = Modifier.testTag("errorPopupText")) },
        confirmButton = {
            MediButton(onClick = onDismiss) {
                Text("Ok")
            }
        },
        modifier = modifier
    )
}
