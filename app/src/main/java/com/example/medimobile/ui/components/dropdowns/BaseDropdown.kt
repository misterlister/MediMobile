package com.example.medimobile.ui.components.dropdowns

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.constants.DropdownConstants.NOT_SET
import com.example.medimobile.data.utils.isDataEmptyOrNull
import com.example.medimobile.ui.util.highlightIf

@Composable
fun BaseDropdown(
    modifier: Modifier = Modifier,
    currentSelection: String? = null,
    options: List<String>?,
    dropdownLabel: String,
    onSelectionChanged: (String?) -> Unit,
    width: Float = 1f,
    notNullable: Boolean = false,
    emptyHighlight: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    val displayValue = currentSelection ?: NOT_SET
    val dropdownOptions = if (notNullable) {
        options ?: emptyList()
    } else {
        listOf(NOT_SET) + (options ?: emptyList())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(width)
    ) {
        Text(
            text = dropdownLabel,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Surface(
            shape = RoundedCornerShape(6.dp),
            tonalElevation = 1.dp,
            border = BorderStroke(1.dp, colorScheme.outline),
            modifier = modifier
                .highlightIf(emptyHighlight && isDataEmptyOrNull(currentSelection))
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = displayValue,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Open dropdown")
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .widthIn(min = 200.dp)
        ) {
            if (options.isNullOrEmpty()) {
                DropdownMenuItem(
                    text = { Text("No options available") },
                    onClick = {},
                    enabled = false
                )
            } else {
                dropdownOptions.forEach { optionChoice ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = optionChoice,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            onSelectionChanged(if (optionChoice == NOT_SET) null else optionChoice)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BaseDropdownPreview() {
    BaseDropdown(currentSelection = "test", options = listOf("test"), dropdownLabel = "test", onSelectionChanged = {})
}