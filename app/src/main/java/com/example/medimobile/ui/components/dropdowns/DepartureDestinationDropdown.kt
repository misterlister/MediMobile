package com.example.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import com.example.medimobile.data.utils.ShambhalaDropdowns
import com.example.medimobile.ui.components.templates.BaseDropdown

@Composable
fun DepartureDestinationDropdown(
    currentDisplayValue: String,
    onChanged: (String) -> Unit
) {
    BaseDropdown(
        currentSelection = currentDisplayValue,
        options = ShambhalaDropdowns.departureDestinations,
        dropDownLabel = "Departure Destination",
        onSelectionChanged = onChanged
    )
}