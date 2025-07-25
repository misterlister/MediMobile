package com.mgm.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mgm.medimobile.data.model.PatientEncounter
import com.mgm.medimobile.data.utils.toDisplayValues
import com.mgm.medimobile.ui.components.dropdowns.DropdownWithOtherField
import com.mgm.medimobile.ui.components.errorscreens.NoEncounterError
import com.mgm.medimobile.ui.components.errorscreens.NoEventError
import com.mgm.medimobile.ui.components.inputfields.AgeInputField
import com.mgm.medimobile.ui.components.inputfields.DateTimeSelector
import com.mgm.medimobile.ui.components.inputfields.QRScannerButton
import com.mgm.medimobile.ui.components.templates.DividedFormSections
import com.mgm.medimobile.ui.components.templates.FormSectionData
import com.mgm.medimobile.ui.components.templates.MediButton
import com.mgm.medimobile.ui.components.templates.MediTextField
import com.mgm.medimobile.ui.theme.ButtonStatus
import com.mgm.medimobile.viewmodel.MediMobileViewModel
import kotlinx.coroutines.launch

@Composable
fun ArrivalScreen(viewModel: MediMobileViewModel) {
    val encounter = viewModel.currentEncounter.collectAsState().value
    val selectedEvent = viewModel.selectedEvent.collectAsState().value
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    val scannedEncounter = remember { mutableStateOf<PatientEncounter?>(null) }
    val showQRExistingDialog = remember { mutableStateOf(false)}

    fun handleVisitIdInput(scannedValue: String) {
        coroutineScope.launch {
            val match = viewModel.getEncounterByVisitId(scannedValue)
            if (match == null) {
                viewModel.setVisitId(scannedValue)
            } else {
                scannedEncounter.value = match
                showQRExistingDialog.value = true
            }
        }
    }

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
                        handleVisitIdInput(scannedValue)
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

    // Show pop-up dialog if a QR is scanned which is already assigned to an Encounter
    if (showQRExistingDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when clicking outside
                scannedEncounter.value = null
                showQRExistingDialog.value = false
            },
            title = { Text("Encounter Exists") },
            text = {
                Text("An encounter with this Visit ID already exists, would you like to open it?")
            },
            confirmButton = {
                MediButton(
                    onClick = {
                        // Open the encounter with this Visit ID
                        viewModel.setCurrentEncounter(
                            patientEncounter = scannedEncounter.value?: PatientEncounter(),
                            lockVisitId = true
                        )
                        scannedEncounter.value = null
                        showQRExistingDialog.value = false
                    }
                ) {
                    Text("Open Encounter")
                }
            },
            dismissButton = {
                MediButton(
                    onClick = {
                        // Dismiss the dialog if "No" is clicked
                        scannedEncounter.value = null
                        showQRExistingDialog.value = false
                    },
                    status = ButtonStatus.WARNING,
                ) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArrivalScreenPreview() {
    val viewModel = MediMobileViewModel()
    viewModel.initNewEncounter()
    ArrivalScreen(viewModel = viewModel)
}