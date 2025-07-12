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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.dropdowns.DropdownWithOtherField
import com.example.medimobile.ui.components.errorscreens.NoEncounterError
import com.example.medimobile.ui.components.errorscreens.NoEventError
import com.example.medimobile.ui.components.inputfields.AgeInputField
import com.example.medimobile.ui.components.inputfields.DateTimeSelector
import com.example.medimobile.ui.components.inputfields.QRScannerButton
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.components.templates.MediButton
import com.example.medimobile.ui.components.templates.MediTextField
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun ArrivalScreen(viewModel: MediMobileViewModel) {
    val encounter = viewModel.currentEncounter.collectAsState().value
    val selectedEvent = viewModel.selectedEvent.collectAsState().value
    val focusManager = LocalFocusManager.current

    if (encounter == null) {
        NoEncounterError()
    } else if (selectedEvent == null) {
        NoEventError()
    } else {
        val formSections = listOf(
            FormSectionData(
                title = "Arrival Time",
                required = true
                ) {
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
                    },
                    emptyHighlight = true
                )
            },
            FormSectionData(
                title = "Visit ID",
                required = true
            ) {
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
                        modifier = Modifier.weight(0.5f)
                            .testTag("visitIdScannerButton"),
                        enabled = isEnabled,
                        emptyHighlight = encounter.visitId == ""
                    )

                    MediTextField(
                        value = encounter.visitId,
                        onValueChange = { viewModel.setVisitId(it) },
                        placeholder = {
                            Text(
                                "Scan/Gen. visit ID"
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
                        modifier = Modifier.weight(1f)
                            .testTag("visitIdTextField"),
                        enabled = isEnabled,
                        emptyHighlight = encounter.visitId == ""
                    )

                    MediButton(onClick = { viewModel.generateVisitID() },
                        modifier = Modifier.weight(0.5f)
                            .testTag("generateVisitIdButton"),
                        enabled = isEnabled,
                        emptyHighlight = encounter.visitId == ""
                    ) {
                        Text(
                            text = "Gen. Visit ID",
                            textAlign = TextAlign.Center)
                    }
                }
            },
            FormSectionData(
                title = "Arrival Method",
                required = false
            ) {
                DropdownWithOtherField (
                    currentSelection = encounter.arrivalMethod,
                    options = selectedEvent.dropdowns.arrivalMethods.toDisplayValues(),
                    dropdownLabel = "Arrival Method",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setArrivalMethod(newDisplayValue?: "")
                        focusManager.clearFocus()
                    },
                    emptyHighlight = true,
                    modifier = Modifier.testTag("arrivalMethodDropdown")
                )
            },
            FormSectionData(
                title = "Age",
                required = false
            ) {
                AgeInputField(
                    age = encounter.age,
                    onAgeChange = { newAge ->
                        viewModel.setAge(newAge)
                    },
                    emptyHighlight = true,
                    modifier = Modifier.testTag("ageInputField")
                )
            },
            FormSectionData(
                title = "Role",
                required = false
            ) {
                DropdownWithOtherField (
                    currentSelection = encounter.role,
                    options = selectedEvent.dropdowns.roles.toDisplayValues(),
                    dropdownLabel = "Role",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setRole(newDisplayValue?: "")
                        focusManager.clearFocus()
                    },
                    emptyHighlight = true,
                    modifier = Modifier.testTag("roleDropdown")
                )
            },
        )
        DividedFormSections(formSections = formSections)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArrivalScreenPreview() {
    var viewModel = MediMobileViewModel()
    viewModel.initNewEncounter()
    ArrivalScreen(viewModel = viewModel)
}