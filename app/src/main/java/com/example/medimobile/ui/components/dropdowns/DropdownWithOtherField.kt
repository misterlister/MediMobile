package com.example.medimobile.ui.components.dropdowns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.constants.DropdownConstants
import com.example.medimobile.ui.components.templates.MediTextField
import com.example.medimobile.ui.theme.placeholderTextStyle
import com.example.medimobile.ui.util.highlightIf

@Composable
fun DropdownWithOtherField(
    currentSelection: String?,
    options: List<String>,
    dropdownLabel: String,
    emptyHighlight: Boolean = false,
    modifier: Modifier = Modifier,
    otherFieldModifier: Modifier = Modifier,
    onSelectionChanged: (String?) -> Unit,
) {
    var otherText by remember { mutableStateOf("") }

    var currentDropdownSelection by remember { mutableStateOf(currentSelection?.let {
        if (it.startsWith(DropdownConstants.OTHER_PREFIX)) "Other" else it }) }

    val isOtherSelected = currentDropdownSelection == "Other"

    LaunchedEffect(currentSelection) {
        if (currentSelection?.startsWith(DropdownConstants.OTHER_PREFIX) == true) {
            otherText = currentSelection.removePrefix(DropdownConstants.OTHER_PREFIX)
        }
    }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            BaseDropdown(
                currentSelection = if (isOtherSelected) "Other" else currentSelection,
                options = options,
                dropdownLabel = dropdownLabel,
                onSelectionChanged = { newValue ->
                    currentDropdownSelection = newValue
                    if (newValue != "Other") {
                        onSelectionChanged(newValue)
                    } else if (otherText.isNotBlank()) {
                        onSelectionChanged("${DropdownConstants.OTHER_PREFIX}$otherText")
                    } else {
                        onSelectionChanged(null)
                    }
                },
                emptyHighlight = emptyHighlight,
                modifier = modifier
            )
        }
        MediTextField(
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
            modifier = otherFieldModifier
                .highlightIf(emptyHighlight && isOtherSelected && otherText.isEmpty())
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && isOtherSelected && otherText.isNotBlank()) {
                        onSelectionChanged("${DropdownConstants.OTHER_PREFIX}$otherText")
                    }
                },
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