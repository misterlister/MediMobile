package com.example.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import com.example.medimobile.data.utils.ShambhalaDropdowns

@Composable
fun ArrivalMethodDropdown(
    currentMethodDisplayValue: String,
    onMethodChanged: (String) -> Unit
) {
    BaseDropdown(
        currentSelection = currentMethodDisplayValue,
        options = ShambhalaDropdowns.arrivalMethods,
        dropDownLabel = "Arrival Method",
        onSelectionChanged = onMethodChanged
    )
}