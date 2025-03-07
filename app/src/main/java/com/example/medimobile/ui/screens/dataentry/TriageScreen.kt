package com.example.medimobile.ui.screens.dataentry

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.components.AdjustableFormFields
import com.example.medimobile.ui.components.FormSectionData
import com.example.medimobile.ui.components.HourDropdown
import com.example.medimobile.ui.components.MinuteDropdown
import com.example.medimobile.ui.components.RadioButtonWithText
import com.example.medimobile.viewmodel.PatientEncounterViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TriageScreen(viewModel: PatientEncounterViewModel, isLandscape: Boolean = false) {
    val encounter = viewModel.encounter.value

    // Data entry state variables
    val datePickerDialogState = remember { mutableStateOf(false) }
    val expandedArrivalMethodState = remember { mutableStateOf(false) }


    // Show DatePickerDialog when state is true
    if (datePickerDialogState.value) {
        val datePicker = DatePickerDialog(
            LocalContext.current,
            { _, year, month, dayOfMonth ->
                val newDate = LocalDate.of(year, month + 1, dayOfMonth)  // Month is zero-indexed
                viewModel.setArrivalDate(newDate)  // Update ViewModel directly
            },
            encounter.arrivalDate.year,
            encounter.arrivalDate.monthValue - 1,  // Month is zero-indexed
            encounter.arrivalDate.dayOfMonth
        )
        datePicker.show()
        datePickerDialogState.value = false // Close dialog after showing
    }

    val formSections = listOf(
        FormSectionData("Arrival Time") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Increase spacing for better separation
            ) {
                // "Now" button column
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Set to Now", fontWeight = FontWeight.Bold)
                    Button(onClick = {
                        val nowDate = LocalDate.now()
                        val nowTime = LocalTime.now()
                        viewModel.setArrivalDate(nowDate)
                        viewModel.setArrivalTime(nowTime)
                    }) {
                        Text(text = "Now")
                    }
                }

                // Date selection column
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Select Date", fontWeight = FontWeight.Bold)
                    Button(onClick = { datePickerDialogState.value = true }) {
                        Text(text = encounter.arrivalDate.toString())
                    }
                }

                // Time input column
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Enter Time", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    HourDropdown(
                        hour = encounter.arrivalTime.hour,
                        onHourChanged = { newHour ->
                            val newTime = encounter.arrivalTime.withHour(newHour)
                            viewModel.setArrivalTime(newTime)
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MinuteDropdown(
                        minute = encounter.arrivalTime.minute,
                        onMinuteChanged = { newMinute ->
                            val newTime = encounter.arrivalTime.withMinute(newMinute)
                            viewModel.setArrivalTime(newTime)
                        }
                    )
                }
            }
        },
        FormSectionData("Triage") {
            val selectedOption = remember { mutableStateOf<String?>(null) }
            TriageRadioButtons(isLandscape, selectedOption = selectedOption.value, onOptionSelected = { selectedOption.value = it })
        },
        FormSectionData("Visit ID") {
            TextField(value = "", onValueChange = {}, placeholder = { Text("Enter Visit ID") })
        },
        FormSectionData("Arrival Method") {
            Box(modifier = Modifier.wrapContentSize()) {
                Column {
                    // Button to toggle the DropdownMenu expansion
                    Button(onClick = { expandedArrivalMethodState.value = !expandedArrivalMethodState.value }) {
                        Text(text = "Select Arrival Method")
                    }
                    // DropdownMenu that uses the expanded state to show/hide
                    DropdownMenu(
                        expanded = expandedArrivalMethodState.value,
                        onDismissRequest = { expandedArrivalMethodState.value = false }
                    ) {
                        DropdownMenuItem(text = { Text("Ambulance") }, onClick = { expandedArrivalMethodState.value = false })
                        DropdownMenuItem(text = { Text("Walk-in") }, onClick = { expandedArrivalMethodState.value = false })
                    }
                }
            }
        }
    )
    AdjustableFormFields(formSections = formSections, isLandscape = isLandscape)
}

@Composable
fun TriageRadioButtons(isLandscape: Boolean = false, selectedOption: String?, onOptionSelected: (String) -> Unit) {
    val options = listOf("Green", "Yellow", "Red", "White")

    // Arrange in grid format for landscape orientation
    if (isLandscape) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    RadioButtonWithText(option = options[0], selectedOption, onOptionSelected)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    RadioButtonWithText(option = options[1], selectedOption, onOptionSelected)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    RadioButtonWithText(option = options[2], selectedOption, onOptionSelected)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    RadioButtonWithText(option = options[3], selectedOption, onOptionSelected)
                }
            }
        }

        // Arrange in row format for portrait orientation
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEach { option ->
                RadioButtonWithText(option, selectedOption, onOptionSelected)
            }
        }
    }
}