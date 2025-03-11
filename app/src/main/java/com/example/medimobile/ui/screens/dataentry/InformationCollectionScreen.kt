package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.components.templates.AdjustableFormFields
import com.example.medimobile.ui.components.templates.FormSectionData
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
                currentDisplayValue = encounter.role,
                onChanged = { newDisplayValue ->
                    viewModel.setRole(newDisplayValue)
                }
            )
        },
        FormSectionData("Chief Complaint") {
            ChiefComplaintDropdown(
                currentDisplayValue = encounter.chiefComplaint,
                onChanged = { newDisplayValue ->
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