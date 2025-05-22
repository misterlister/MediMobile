package com.example.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import com.example.medimobile.data.constants.DropdownConstants


@Composable
fun HourDropdown(
    currentHour: String?,
    emptyHighlight: Boolean = false,
    onHourChanged: (String?) -> Unit
) {
    BaseDropdown(
        currentSelection = currentHour,
        options = DropdownConstants.HOURS,
        dropdownLabel = "Select Hour",
        onSelectionChanged = { newSelection ->
            onHourChanged(newSelection)
        },
        emptyHighlight = emptyHighlight
    )
}

@Composable
fun MinuteDropdown(
    currentMinute: String?,
    highlight: Boolean = false,
    onMinuteChanged: (String?) -> Unit
) {
    BaseDropdown(
        currentSelection = currentMinute,
        options = DropdownConstants.MINUTES,
        dropdownLabel = "Select Minute",
        onSelectionChanged = { newSelection ->
            onMinuteChanged(newSelection)
        },
        emptyHighlight = highlight
    )
}
