package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.components.dropdowns.ArrivalMethodDropdown
import com.example.medimobile.ui.components.dropdowns.DepartureDestinationDropdown
import com.example.medimobile.ui.components.inputfields.DateTimeSelector
import com.example.medimobile.ui.components.templates.AdjustableFormFields
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.viewmodel.PatientEncounterViewModel

@Composable
fun DischargeScreen(viewModel: PatientEncounterViewModel, isLandscape: Boolean = false) {
    val encounter = viewModel.encounter.value
    val formSections = listOf(
        FormSectionData("Departure Time") {
            DateTimeSelector(
                encounter.departureDate,
                encounter.departureTime,
                onDateChange = { viewModel.setDepartureDate(it) },
                onTimeChange = { viewModel.setDepartureTime(it) }
            )
        },
        FormSectionData("Departure Destination") {
            DepartureDestinationDropdown(
                currentDisplayValue = encounter.departureDest,  // Initial value
                onChanged = { newDisplayValue ->
                    viewModel.setDepartureDest(newDisplayValue)  // Store the displayValue
                }
            )
        },
        FormSectionData("Discharge Diagnosis") {
            TextField(
                value = encounter.dischargeDiagnosis, // Bind to ViewModel
                onValueChange = { viewModel.setDischargeDiagnosis(it) }, // Update ViewModel
                placeholder = { Text("Enter Discharge Diagnosis (optional)") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(150.dp)
            )
        },
    )
    AdjustableFormFields(formSections = formSections, isLandscape = isLandscape)
}