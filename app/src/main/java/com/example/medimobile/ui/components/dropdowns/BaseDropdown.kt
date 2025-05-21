package com.example.medimobile.ui.components.dropdowns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.constants.DropdownConstants.NOT_SET


@Composable
fun BaseDropdown(
    currentSelection: String? = null,
    options: List<String>?,
    dropdownLabel: String,
    onSelectionChanged: (String?) -> Unit,
    width: Float = 1f,
    notNullable: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val displayValue = currentSelection ?: NOT_SET
    val dropdownOptions = if (notNullable) {
        options ?: emptyList()
    } else {
        listOf(NOT_SET) + (options ?: emptyList())
    }

    // OutlinedTextField for the clickable area
    OutlinedTextField(
        value = displayValue,
        onValueChange = {},
        label = { Text(
            text = dropdownLabel,
            style = MaterialTheme.typography.bodySmall.copy(color = colorScheme.onSurface))
        },
        enabled = false, // Disable the text field so it can't be edited
        colors = OutlinedTextFieldDefaults.colors(
            // Restore colour to the disabled text field
            disabledTextColor = colorScheme.onSurface,
            disabledContainerColor = colorScheme.primaryContainer,
            disabledBorderColor = colorScheme.outline,
            disabledLeadingIconColor = colorScheme.onSurface,
            disabledTrailingIconColor = colorScheme.onSurface,
            disabledLabelColor = colorScheme.onSurface,
            disabledPlaceholderColor = colorScheme.onSurface,
            disabledSupportingTextColor = colorScheme.onSurface,
            disabledPrefixColor = colorScheme.onSurface,
            disabledSuffixColor = colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth(width)
            .clickable{ expanded = true }
            .padding(bottom = 8.dp),


        trailingIcon = { Icon(Icons.Default.ArrowDropDown,
            contentDescription = "Select Options",
        )},
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = modifier
            .width(240.dp) // Set width for dropdown
            .background(colorScheme.secondaryContainer)
    ) {
        if (options.isNullOrEmpty()) {
            DropdownMenuItem(
                text = { Text("No options available") },
                enabled = false, // Make this item not clickable
                onClick = {}
            )
        } else {
            dropdownOptions.forEach { optionChoice ->
                DropdownMenuItem(
                    text = { Text(optionChoice) },
                    onClick = {
                        onSelectionChanged(if (optionChoice == NOT_SET) null else optionChoice)
                        expanded = false // Close dropdown after selection
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BaseDropdownPreview() {
    BaseDropdown("test", listOf("test"), "test", {})
}