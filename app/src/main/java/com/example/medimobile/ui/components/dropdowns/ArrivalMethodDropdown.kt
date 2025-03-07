package com.example.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import com.example.medimobile.data.utils.ShambhalaDropdowns

@Composable
fun ArrivalMethodDropdown(
    currentMethodDisplayValue: String,  // This holds the displayValue
    onMethodChanged: (String) -> Unit  // This will be triggered with displayValue
) {
    BaseDropdown(
        currentSelection = currentMethodDisplayValue,
        options = ShambhalaDropdowns.arrivalMethods,  // Pass the list of DropdownItems
        dropDownLabel = "Arrival Method",
        onSelectionChanged = onMethodChanged  // Pass the displayValue when a new method is selected
    )
}