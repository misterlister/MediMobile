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
fun DischargeScreen(viewModel: PatientEncounterViewModel, isLandscape: Boolean = false) {
    val encounter = viewModel.encounter.value
    val expandedDestinationState = remember { mutableStateOf(false) }
    val formSections = listOf(
        FormSectionData("Departure Time") {
            TextField(value = "", onValueChange = {}, placeholder = { Text("Enter age") })
        },
        FormSectionData("Departure Destination") {
            Box(modifier = Modifier.wrapContentSize()) {
                Column {
                    // Button to toggle the DropdownMenu expansion
                    Button(onClick = { expandedDestinationState.value = !expandedDestinationState.value }) {
                        Text(text = "Select Arrival Method")
                    }
                    DropdownMenu(
                        expanded = expandedDestinationState.value,
                        onDismissRequest = { expandedDestinationState.value = false }
                    ) {
                        DropdownMenuItem(text = { Text("Back to Event") }, onClick = { expandedDestinationState.value = false })
                        DropdownMenuItem(text = { Text("Hospital via ambulance") }, onClick = { expandedDestinationState.value = false })
                    }
                }
            }
        },
        FormSectionData("Discharge Diagnosis") {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter Discharge Diagnosis (optional)") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(150.dp)
            )
        },
    )
    AdjustableFormFields(formSections = formSections, isLandscape = isLandscape)
}