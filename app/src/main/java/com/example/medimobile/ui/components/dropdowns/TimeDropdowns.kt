package com.example.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.medimobile.data.constants.DropdownConstants


@Composable
fun HourDropdown(
    currentHour: String?,
    emptyHighlight: Boolean = false,
    modifier: Modifier = Modifier,
    onHourChanged: (String?) -> Unit
) {
    BaseDropdown(
        currentSelection = currentHour,
        options = DropdownConstants.HOURS,
        dropdownLabel = "Select Hour",
        onSelectionChanged = { newSelection ->
            onHourChanged(newSelection)
        },
        emptyHighlight = emptyHighlight,
        modifier = modifier.testTag("hourDropdown")
    )
}

@Composable
fun MinuteDropdown(
    currentMinute: String?,
    highlight: Boolean = false,
    modifier: Modifier = Modifier,
    onMinuteChanged: (String?) -> Unit
) {
    BaseDropdown(
        currentSelection = currentMinute,
        options = DropdownConstants.MINUTES,
        dropdownLabel = "Select Minute",
        onSelectionChanged = { newSelection ->
            onMinuteChanged(newSelection)
        },
        emptyHighlight = highlight,
        modifier = modifier.testTag("minuteDropdown")
    )
}
