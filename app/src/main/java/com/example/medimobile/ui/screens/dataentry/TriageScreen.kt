package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.errorscreens.NoEncounterError
import com.example.medimobile.ui.components.errorscreens.NoEventError
import com.example.medimobile.ui.components.inputfields.DateTimeSelector
import com.example.medimobile.ui.components.inputfields.TriageRadioButtons
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.DropdownWithOtherField
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.theme.placeholderTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun TriageScreen(viewModel: MediMobileViewModel) {
    val encounter = viewModel.currentEncounter.collectAsState().value
    val selectedEvent = viewModel.selectedEvent.collectAsState().value
    val focusManager = LocalFocusManager.current

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
                    onDateChange = {
                        viewModel.setArrivalDate(it)
                        focusManager.clearFocus()
                    },
                    onTimeChange = {
                        viewModel.setArrivalTime(it)
                        focusManager.clearFocus()
                    }
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
                    placeholder = {
                        Text(
                            "Enter Visit ID",
                            style = placeholderTextStyle
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                )
            },
            FormSectionData("Arrival Method") {
                DropdownWithOtherField (
                    currentSelection = encounter.arrivalMethod,
                    options = selectedEvent.dropdowns.arrivalMethods.toDisplayValues(),
                    dropdownLabel = "Arrival Method",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setArrivalMethod(newDisplayValue)
                        focusManager.clearFocus()
                    }
                )
            }
        )
        DividedFormSections(formSections = formSections)
    }
}
