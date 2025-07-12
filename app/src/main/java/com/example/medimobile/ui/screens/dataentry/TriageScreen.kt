package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.medimobile.R
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.dropdowns.DropdownWithOtherField
import com.example.medimobile.ui.components.errorscreens.NoEncounterError
import com.example.medimobile.ui.components.errorscreens.NoEventError
import com.example.medimobile.ui.components.inputfields.TriageRadioButtons
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.components.templates.MediTextField
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
            FormSectionData(
                title = "Triage",
                required = false
            ) {
                TriageRadioButtons(
                    selectedOption = encounter.triageAcuity,
                    onOptionSelected = {
                        if (it == null) {
                            viewModel.setTriageAcuity("")
                        } else {
                            viewModel.setTriageAcuity(it)
                        }
                    },
                    emptyHighlight = true
                )
            },
            FormSectionData(
                title = "Chief Complaint",
                required = false
            ) {
                DropdownWithOtherField (
                    currentSelection = encounter.chiefComplaint,
                    options = selectedEvent.dropdowns.chiefComplaints.toDisplayValues(),
                    dropdownLabel = "Chief Complaint",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setChiefComplaint(newDisplayValue?: "")
                        focusManager.clearFocus()
                    },
                    emptyHighlight = true,
                    modifier = Modifier.testTag("chiefComplaintDropdown")
                )
            },
            FormSectionData(
                title = "Comments",
                required = false
            ) {
                MediTextField(
                    value = encounter.comment, // Bind to ViewModel
                    onValueChange = {
                        viewModel.setComment(it)
                    },
                    placeholder = {
                        Column(){
                            Text(
                                text = "Enter comments (optional)"
                            )
                            Text(
                                text = stringResource(R.string.no_personal_info_warning),
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
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
                        .testTag("commentsTextField"),
                )
            },
        )
        DividedFormSections(formSections = formSections)
    }
}