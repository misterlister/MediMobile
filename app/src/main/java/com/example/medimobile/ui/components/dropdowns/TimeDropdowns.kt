package com.example.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import com.example.medimobile.data.utils.DropdownConstants


@Composable
fun HourDropdown(
    currentHour: String,
    onHourChanged: (String) -> Unit
) {
    // Use the HOURS list from DropdownConstants
    BaseDropdown(
        currentSelection = currentHour,
        options = DropdownConstants.HOURS,  // Pass the list of hours
        dropDownLabel = "Hour",
        onSelectionChanged = onHourChanged
    )
}

@Composable
fun MinuteDropdown(
    currentMinute: String,
    onMinuteChanged: (String) -> Unit
) {
    // Use the MINUTES list from DropdownConstants
    BaseDropdown(
        currentSelection = currentMinute,
        options = DropdownConstants.MINUTES,  // Pass the list of minutes
        dropDownLabel = "Minute",
        onSelectionChanged = onMinuteChanged
    )
}
