package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.errorscreens.NoEncounterError
import com.example.medimobile.ui.components.errorscreens.NoEventError
import com.example.medimobile.ui.components.inputfields.AgeInputField
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.DropdownWithOtherField
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.theme.placeholderTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun InformationCollectionScreen(viewModel: MediMobileViewModel) {
    val encounter = viewModel.currentEncounter.collectAsState().value
    val selectedEvent = viewModel.selectedEvent.collectAsState().value
    val focusManager = LocalFocusManager.current


    if (encounter == null) {
        NoEncounterError()
    } else if (selectedEvent == null) {
        NoEventError()
    } else {
        val formSections = listOf(
            FormSectionData("Age") {
                AgeInputField(
                    age = encounter.age,
                    onAgeChange = { newAge ->
                        viewModel.setAge(newAge)
                    }
                )
            },
            FormSectionData("Role") {
                DropdownWithOtherField (
                    currentSelection = encounter.role,
                    options = selectedEvent.dropdowns.roles.toDisplayValues(),
                    dropdownLabel = "Role",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setRole(newDisplayValue)
                        focusManager.clearFocus()
                    }
                )
            },
            FormSectionData("Chief Complaint") {
                DropdownWithOtherField (
                    currentSelection = encounter.chiefComplaint,
                    options = selectedEvent.dropdowns.chiefComplaints.toDisplayValues(),
                    dropdownLabel = "Chief Complaint",
                    onSelectionChanged = { newDisplayValue ->
                        viewModel.setChiefComplaint(newDisplayValue)
                        focusManager.clearFocus()
                    }
                )
            },
            FormSectionData("Comments") {
                TextField(
                    value = encounter.comment, // Bind to ViewModel
                    onValueChange = { viewModel.setComment(it) }, // Update ViewModel
                    placeholder = {
                        Text(
                            text = "Enter comments (optional)",
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
                )
            },
        )
        DividedFormSections(formSections = formSections)
    }
}