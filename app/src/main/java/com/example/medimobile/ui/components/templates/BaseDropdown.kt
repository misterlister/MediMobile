package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BaseDropdown(
    currentSelection: String,
    options: List<String>?,
    dropDownLabel: String,
    onSelectionChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        // OutlinedTextField for the clickable area
        OutlinedTextField(
            value = currentSelection,
            onValueChange = {},
            label = { Text(
                text = dropDownLabel,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Black))
            },
            enabled = false, // Disable the text field so it can't be edited
            colors = OutlinedTextFieldDefaults.colors(
                // Restore colour to the disabled text field
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = Color.White,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable{ expanded = true }, // Make area clickable

            trailingIcon = { Icon(Icons.Default.ArrowDropDown,
                contentDescription = "Select Options",
            )},
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(240.dp) // Set width for dropdown
                .background(Color.White) // Background color for the dropdown menu
        ) {
            if (options.isNullOrEmpty()) {
                DropdownMenuItem(
                    text = { Text("No options available") },
                    enabled = false, // Make this item not clickable
                    onClick = {}
                )
            } else {
                options.forEach { optionChoice ->
                    DropdownMenuItem(
                        text = { Text(optionChoice) },
                        onClick = {
                            onSelectionChanged(optionChoice) // Update selection
                            expanded = false // Close dropdown after selection
                        }
                    )
                }
            }
        }
    }
}