package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.components.AdjustableFormFields
import com.example.medimobile.ui.components.FormSectionData
import com.example.medimobile.viewmodel.PatientEncounterViewModel

@Composable
fun InformationCollectionScreen(viewModel: PatientEncounterViewModel, isLandscape: Boolean = false) {
    val encounter = viewModel.encounter.value
    val expandedRoleState = remember { mutableStateOf(false) }
    val expandedChiefComplaintState = remember { mutableStateOf(false) }
    val formSections = listOf(
        FormSectionData("Age") {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter age") },
                modifier = Modifier.fillMaxWidth(0.4f)
            )
        },
        FormSectionData("Role") {
            Box(modifier = Modifier.wrapContentSize()) {
                Column {
                    // Button to toggle the DropdownMenu expansion
                    Button(onClick = { expandedRoleState.value = !expandedRoleState.value }) {
                        Text(text = "Select Arrival Method")
                    }
                    // DropdownMenu that uses the expanded state to show/hide
                    DropdownMenu(
                        expanded = expandedRoleState.value,
                        onDismissRequest = { expandedRoleState.value = false }
                    ) {
                        DropdownMenuItem(text = { Text("Staff") }, onClick = { expandedRoleState.value = false })
                        DropdownMenuItem(text = { Text("Attendee") }, onClick = { expandedRoleState.value = false })
                    }
                }
            }
        },
        FormSectionData("Chief Complaint") {
            Box(modifier = Modifier.wrapContentSize()) {
                Column {
                    // Button to toggle the DropdownMenu expansion
                    Button(onClick = { expandedChiefComplaintState.value = !expandedChiefComplaintState.value }) {
                        Text(text = "Select Chief Complaint")
                    }
                    // DropdownMenu that uses the expanded state to show/hide
                    DropdownMenu(
                        expanded = expandedChiefComplaintState.value,
                        onDismissRequest = { expandedChiefComplaintState.value = false }
                    ) {
                        DropdownMenuItem(text = { Text("Nausea") }, onClick = { expandedChiefComplaintState.value = false })
                        DropdownMenuItem(text = { Text("Dizziness") }, onClick = { expandedChiefComplaintState.value = false })
                    }
                }
            }
        },
        FormSectionData("Comments") {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter comments (optional)") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(150.dp)
            )
        },
    )
    AdjustableFormFields(formSections = formSections, isLandscape = isLandscape)
}