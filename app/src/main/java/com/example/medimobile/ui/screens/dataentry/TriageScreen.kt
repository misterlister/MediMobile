package com.example.medimobile.ui.screens.dataentry

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.errorscreens.NoEncounterError
import com.example.medimobile.ui.components.errorscreens.NoEventError
import com.example.medimobile.ui.components.inputfields.DateTimeSelector
import com.example.medimobile.ui.components.inputfields.TriageRadioButtons
import com.example.medimobile.ui.components.templates.BaseDropdown
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun TriageScreen(viewModel: MediMobileViewModel) {
    val encounter = viewModel.currentEncounter.collectAsState().value
    val selectedEvent = viewModel.selectedEvent.collectAsState().value

    if (encounter == null) {
        NoEncounterError()
    } else if (selectedEvent == null) {
        NoEventError()
    } else {
        val formSections = listOf(
            FormSectionData("Arrival Time") {
                DateTimeSelector(
                    encounter.arrivalDate,
                    encounter.arrivalTime,
                    onDateChange = { viewModel.setArrivalDate(it) },
                    onTimeChange = { viewModel.setArrivalTime(it) }
                )
            },
            FormSectionData("Triage") {
                TriageRadioButtons(
                    selectedOption = encounter.triageAcuity,
                    onOptionSelected = { viewModel.setTriageAcuity(it) }
                )
            },
            FormSectionData("Visit ID") {
                TextField(
                    value = encounter.visitId,
                    onValueChange = { viewModel.setVisitId(it) },
                    placeholder = { Text("Enter Visit ID") }
                )
            },
            FormSectionData("Arrival Method") {
                BaseDropdown (
                    currentSelection = encounter.arrivalMethod,
                    options = selectedEvent.dropdowns.arrivalMethods.toDisplayValues(),
                    dropDownLabel = "Arrival Method",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setArrivalMethod(newDisplayValue)
                    }
                )
            }
        )
        DividedFormSections(formSections = formSections)
    }
}
