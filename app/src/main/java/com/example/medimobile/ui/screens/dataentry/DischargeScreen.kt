package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.dropdowns.DropdownWithOtherField
import com.example.medimobile.ui.components.errorscreens.NoEncounterError
import com.example.medimobile.ui.components.errorscreens.NoEventError
import com.example.medimobile.ui.components.inputfields.DateTimeSelector
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.components.templates.MediTextField
import com.example.medimobile.ui.theme.placeholderTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun DischargeScreen(viewModel: MediMobileViewModel) {
    val encounter = viewModel.currentEncounter.collectAsState().value
    val selectedEvent = viewModel.selectedEvent.collectAsState().value
    val focusManager = LocalFocusManager.current

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
                    onDateChange = {
                        viewModel.setDepartureDate(it)
                        focusManager.clearFocus()
                    },
                    onTimeChange = {
                        viewModel.setDepartureTime(it)
                        focusManager.clearFocus()
                    },
                    emptyHighlight = true
                )
            },
            FormSectionData("Departure Destination") {
                DropdownWithOtherField (
                    currentSelection = encounter.departureDest,
                    options = selectedEvent.dropdowns.departureDestinations.toDisplayValues(),
                    dropdownLabel = "Departure Destination",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setDepartureDest(newDisplayValue?: "")
                        focusManager.clearFocus()
                    },
                    emptyHighlight = true,
                    modifier = Modifier.testTag("departureDestinationDropdown")
                )
            },
            FormSectionData("Discharge Diagnosis") {
                MediTextField(
                    value = encounter.dischargeDiagnosis, // Bind to ViewModel
                    onValueChange = {
                        viewModel.setDischargeDiagnosis(it)
                    },
                    placeholder = {
                        Text(
                            text = "Enter Discharge Diagnosis (optional)",
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
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(150.dp)
                        .testTag("dischargeDiagnosisTextField"),
                )
            },
        )
        DividedFormSections(formSections = formSections)
    }
}