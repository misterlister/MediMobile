package com.example.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import com.example.medimobile.data.constants.DropdownConstants
import com.example.medimobile.data.constants.DropdownConstants.NOT_SET


@Composable
fun HourDropdown(
    currentHour: String?,
    onHourChanged: (String?) -> Unit
) {
    BaseDropdown(
        currentSelection = currentHour,
        options = DropdownConstants.HOURS,
        dropdownLabel = "Select Hour",
        onSelectionChanged = { newSelection ->
            onHourChanged(newSelection)
        }
    )
}

@Composable
fun MinuteDropdown(
    currentMinute: String?,
    onMinuteChanged: (String?) -> Unit
) {
    BaseDropdown(
        currentSelection = currentMinute,
        options = DropdownConstants.MINUTES,
        dropdownLabel = "Select Minute",
        onSelectionChanged = { newSelection ->
            onMinuteChanged(newSelection)
        }
    )
}
