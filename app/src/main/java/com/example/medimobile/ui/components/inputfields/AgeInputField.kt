package com.example.medimobile.ui.components.inputfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AgeInputField(age: Int, onAgeChange: (Int) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // FocusRequester to control when the TextField should regain focus
    val focusRequester = remember { FocusRequester() }
    // FocusManager to close the keyboard when Done is pressed
    val focusManager = LocalFocusManager.current

    TextField(
        value = if (isFocused && age == 0) "" else age.toString(),
        onValueChange = { newText: String ->
            val newAge = newText.toIntOrNull() ?: 0
            onAgeChange(newAge)
        },
        placeholder = { Text("Enter age") },
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .focusRequester(focusRequester) // Attach the FocusRequester
            .onFocusChanged { focusState ->
                if (!focusState.isFocused && age == 0) {
                    // Reset the value to 0 if it loses focus with 0 as the value
                    onAgeChange(0)
                }
            },
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
        interactionSource = interactionSource
    )
}
