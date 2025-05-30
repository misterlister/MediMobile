package com.example.medimobile.ui.components.inputfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.medimobile.ui.components.templates.MediTextField

@Composable
fun AgeInputField(
    age: Int?,
    emptyHighlight: Boolean = false,
    modifier: Modifier = Modifier,
    onAgeChange: (Int?) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    // FocusManager to close the keyboard when Done is pressed
    val focusManager = LocalFocusManager.current

    MediTextField(
        value = age?.toString() ?: "",
        onValueChange = { newText: String ->
            onAgeChange(newText.toIntOrNull())
        },
        placeholder = { Text("Enter age") },
        modifier = modifier.fillMaxWidth(0.4f),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                // Close keyboard when Done is pressed
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        interactionSource = interactionSource,
        emptyHighlight = emptyHighlight
    )
}
