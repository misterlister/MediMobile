package com.example.medimobile.ui.components.dropdowns

import androidx.compose.runtime.Composable
import com.example.medimobile.data.utils.ShambhalaDropdowns

@Composable
fun ChiefComplaintDropdown(
    currentMethodDisplayValue: String,
    onMethodChanged: (String) -> Unit
) {
    BaseDropdown(
        currentSelection = currentMethodDisplayValue,
        options = ShambhalaDropdowns.chiefComplaints,
        dropDownLabel = "Chief Complaint",
        onSelectionChanged = onMethodChanged
    )
}