package com.example.medimobile.ui.screens.dataentry

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.medimobile.ui.components.templates.AdjustableFormFields
import com.example.medimobile.ui.components.inputfields.DateTimeSelector
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.components.dropdowns.ArrivalMethodDropdown
import com.example.medimobile.ui.components.inputfields.TriageRadioButtons
import com.example.medimobile.viewmodel.PatientEncounterViewModel

@Composable
fun TriageScreen(viewModel: PatientEncounterViewModel, isLandscape: Boolean = false) {
    val encounter = viewModel.encounter.value

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
                value = encounter.visitId, // Bind to ViewModel
                onValueChange = { viewModel.setVisitId(it) }, // Update ViewModel
                placeholder = { Text("Enter Visit ID") }
            )
        },
        FormSectionData("Arrival Method") {
            ArrivalMethodDropdown(
                currentDisplayValue = encounter.arrivalMethod,  // Initial value
                onChanged = { newDisplayValue ->
                    viewModel.setArrivalMethod(newDisplayValue)  // Store the displayValue
                }
            )
        }
    )
    AdjustableFormFields(formSections = formSections, isLandscape = isLandscape)
}
