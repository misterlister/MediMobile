package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.errorscreens.NoEncounterError
import com.example.medimobile.ui.components.errorscreens.NoEventError
import com.example.medimobile.ui.components.inputfields.DateTimeSelector
import com.example.medimobile.ui.components.templates.BaseDropdown
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun DischargeScreen(viewModel: MediMobileViewModel) {
    val encounter = viewModel.currentEncounter.collectAsState().value
    val selectedEvent = viewModel.selectedEvent.collectAsState().value

    if (encounter == null) {
        NoEncounterError()
    } else if (selectedEvent == null) {
        NoEventError()
    } else {
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
                BaseDropdown (
                    currentSelection = encounter.departureDest,
                    options = selectedEvent.dropdowns.departureDestinations.toDisplayValues(),
                    dropDownLabel = "Departure Destination",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setDepartureDest(newDisplayValue)
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
        DividedFormSections(formSections = formSections)
    }
}