package com.mgm.medimobile.ui.components.dropdowns

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mgm.medimobile.data.constants.DropdownConstants.DROPDOWN_HEIGHT
import com.mgm.medimobile.data.constants.DropdownConstants.DROPDOWN_WIDTH
import com.mgm.medimobile.data.constants.DropdownConstants.NOT_SET
import com.mgm.medimobile.data.constants.DropdownConstants.NO_OPTIONS
import com.mgm.medimobile.data.utils.isDataEmptyOrNull
import com.mgm.medimobile.ui.util.highlightIf


@OptIn(ExperimentalMaterial3Api::class)
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


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth(width)
            .highlightIf(emptyHighlight && isDataEmptyOrNull(currentSelection))
            .widthIn(min = DROPDOWN_WIDTH.dp)
    ) {
        TextField(
            value = if (!isDataEmptyOrNull(displayValue)) displayValue else "",
            onValueChange = { /* Read-only, ignore input */ },
            readOnly = true,
            label = { Text(dropdownLabel) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
                .widthIn(min = DROPDOWN_WIDTH.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .widthIn(min = DROPDOWN_WIDTH.dp)
                .heightIn(max = DROPDOWN_HEIGHT.dp)
        ) {
            if (options.isNullOrEmpty()) {
                DropdownMenuItem(
                    text = { Text(NO_OPTIONS) },
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