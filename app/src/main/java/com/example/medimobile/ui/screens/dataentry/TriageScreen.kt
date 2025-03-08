package com.example.medimobile.ui.screens.dataentry

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
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
import com.example.medimobile.ui.components.RadioButtonWithText
import com.example.medimobile.ui.components.dropdowns.ArrivalMethodDropdown
import com.example.medimobile.ui.components.dropdowns.HourDropdown
import com.example.medimobile.ui.components.dropdowns.MinuteDropdown
import com.example.medimobile.viewmodel.PatientEncounterViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TriageScreen(viewModel: PatientEncounterViewModel, isLandscape: Boolean = false) {
    val encounter = viewModel.encounter.value

    // Data entry state variables
    val datePickerDialogState = remember { mutableStateOf(false) }


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
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 180.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "Now" button column
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Select Date", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { datePickerDialogState.value = true }) {
                        Text(text = encounter.arrivalDate.toString())
                    }
                }

                // Time input column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    HourDropdown(
                        currentHour = "%02d".format(encounter.arrivalTime.hour), // Format the current hour as a string
                        onHourChanged = { newHour ->
                            val newTime = encounter.arrivalTime.withHour(newHour.toInt()) // Convert string to int
                            viewModel.setArrivalTime(newTime)
                        }
                    )

                    MinuteDropdown(
                        currentMinute = "%02d".format(encounter.arrivalTime.minute), // Format the current minute as a string
                        onMinuteChanged = { newMinute ->
                            val newTime = encounter.arrivalTime.withMinute(newMinute.toInt()) // Convert string to int
                            viewModel.setArrivalTime(newTime)
                        }
                    )
                }
            }
        },
        FormSectionData("Triage") {
            TriageRadioButtons(isLandscape, selectedOption = encounter.triageAcuity, onOptionSelected = { viewModel.setTriageAcuity(it) })
        },
        FormSectionData("Visit ID") {
            TextField(
                value = encounter.visitId, // Bind to ViewModel
                onValueChange = { viewModel.setVisitId(it) }, // Update ViewModel
                placeholder = { Text("Enter Visit ID") }
            )
        },
        FormSectionData("Arrival Method") {
            ArrivalMethodDropdown(
                currentMethodDisplayValue = encounter.arrivalMethod,  // Initial value
                onMethodChanged = { newDisplayValue ->
                    viewModel.setArrivalMethod(newDisplayValue)  // Store the displayValue
                }
            )
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