package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.components.AdjustableFormFields
import com.example.medimobile.ui.components.FormSectionData
import com.example.medimobile.ui.components.dropdowns.ChiefComplaintDropdown
import com.example.medimobile.ui.components.dropdowns.RoleDropdown
import com.example.medimobile.ui.components.inputfields.AgeInputField
import com.example.medimobile.viewmodel.PatientEncounterViewModel

@Composable
fun InformationCollectionScreen(viewModel: PatientEncounterViewModel, isLandscape: Boolean = false) {
    val encounter = viewModel.encounter.value
    val formSections = listOf(
        FormSectionData("Age") {
            AgeInputField(
                age = encounter.age,
                onAgeChange = { newAge -> viewModel.setAge(newAge) }
            )
        },
        FormSectionData("Role") {
            RoleDropdown(
                currentMethodDisplayValue = encounter.role,
                onMethodChanged = { newDisplayValue ->
                    viewModel.setRole(newDisplayValue)
                }
            )
        },
        FormSectionData("Chief Complaint") {
            ChiefComplaintDropdown(
                currentMethodDisplayValue = encounter.chiefComplaint,
                onMethodChanged = { newDisplayValue ->
                    viewModel.setChiefComplaint(newDisplayValue)
                }
            )
        },
        FormSectionData("Comments") {
            TextField(
                value = encounter.comment, // Bind to ViewModel
                onValueChange = { viewModel.setComment(it) }, // Update ViewModel
                placeholder = { Text("Enter comments (optional)") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(150.dp)
            )
        },
    )
    AdjustableFormFields(formSections = formSections, isLandscape = isLandscape)
}