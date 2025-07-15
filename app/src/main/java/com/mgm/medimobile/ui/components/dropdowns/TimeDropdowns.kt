package com.mgm.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mgm.medimobile.data.constants.DropdownConstants


@Composable
fun HourDropdown(
    currentHour: String?,
    modifier: Modifier = Modifier,
    emptyHighlight: Boolean = false,
    onHourChanged: (String?) -> Unit
) {
    BaseDropdown(
        currentSelection = currentHour,
        options = DropdownConstants.HOURS,
        dropdownLabel = "Hour",
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
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    onMinuteChanged: (String?) -> Unit
) {
    BaseDropdown(
        currentSelection = currentMinute,
        options = DropdownConstants.MINUTES,
        dropdownLabel = "Minute",
        onSelectionChanged = { newSelection ->
            onMinuteChanged(newSelection)
        },
        emptyHighlight = highlight,
        modifier = modifier.testTag("minuteDropdown")
    )
}
