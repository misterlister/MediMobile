package com.example.medimobile.ui.components.dropdowns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.utils.DropdownConstants
import com.example.medimobile.ui.theme.placeholderTextStyle

@Composable
fun DropdownWithOtherField(
    currentSelection: String,
    options: List<String>,
    dropdownLabel: String,
    onSelectionChanged: (String) -> Unit
) {
    var isOtherSelected by remember { mutableStateOf(currentSelection.startsWith(DropdownConstants.OTHER_PREFIX)) }
    var otherText by remember {
        mutableStateOf(if (isOtherSelected) currentSelection.removePrefix(DropdownConstants.OTHER_PREFIX) else "") }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            BaseDropdown(
                currentSelection = if (isOtherSelected) "Other" else currentSelection,
                options = options,
                dropdownLabel = dropdownLabel,
                onSelectionChanged = { newValue ->
                    if (newValue == "Other") {
                        isOtherSelected = true
                        onSelectionChanged("${DropdownConstants.OTHER_PREFIX}$otherText")
                    } else {
                        isOtherSelected = false
                        onSelectionChanged(newValue)
                    }
                },
            )
        }
        TextField(
            value = otherText,
            onValueChange = {newValue ->
                otherText = newValue
            },
            enabled = isOtherSelected,
            placeholder = {
                Text(
                    text = "Specify Other",
                    style = placeholderTextStyle
                )
            },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        // Only update if "Other" is selected and text is not empty
                        if (isOtherSelected && otherText.isNotEmpty()) {
                            onSelectionChanged("${DropdownConstants.OTHER_PREFIX}$otherText")
                        }
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = TextFieldDefaults.colors().disabledContainerColor
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,

        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DropdownWithOtherFieldPreview() {
    DropdownWithOtherField("test", listOf("test"), "test") { }
}