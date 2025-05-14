package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.dropdowns.DropdownWithOtherField
import com.example.medimobile.ui.components.errorscreens.NoEncounterError
import com.example.medimobile.ui.components.errorscreens.NoEventError
import com.example.medimobile.ui.components.inputfields.DateTimeSelector
import com.example.medimobile.ui.components.inputfields.QRScannerButton
import com.example.medimobile.ui.components.inputfields.TriageRadioButtons
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.components.templates.MediButton
import com.example.medimobile.ui.components.templates.MediTextField
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
            FormSectionData("Visit ID") {
                val isEnabled = !encounter.submitted
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QRScannerButton(onResult = { scannedValue: String ->
                        viewModel.setVisitId(scannedValue)
                    },
                        modifier = Modifier.weight(0.5f),
                        enabled = isEnabled
                    )

                    MediTextField(
                        value = encounter.visitId,
                        onValueChange = { viewModel.setVisitId(it) },
                        placeholder = {
                            Text(
                                "Scan/generate visit ID (Required)",
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
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        enabled = isEnabled
                    )

                    MediButton(onClick = { viewModel.generateVisitID() },
                        modifier = Modifier.weight(0.5f),
                        enabled = isEnabled
                    ) {
                        Text(
                            text = "Gen. Visit ID",
                            textAlign = TextAlign.Center)
                    }
                }
            },
            FormSectionData("Triage") {
                TriageRadioButtons(
                    selectedOption = encounter.triageAcuity,
                    onOptionSelected = { viewModel.setTriageAcuity(it) }
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
