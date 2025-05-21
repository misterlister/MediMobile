package com.example.medimobile.ui.components.inputfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.components.dropdowns.HourDropdown
import com.example.medimobile.ui.components.dropdowns.MinuteDropdown
import com.example.medimobile.ui.components.templates.MediButton
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun DateTimeSelector(
    date: LocalDate?,
    time: LocalTime?,
    onDateChange: (LocalDate?) -> Unit,
    onTimeChange: (LocalTime?) -> Unit
    ) {

    // Keeps track of whether the date selector window is open
    val dateSelectorState = remember { mutableStateOf(false) }

    val selectedHour = remember { mutableStateOf(time?.hour?.let { "%02d".format(it) }) }
    val selectedMinute = remember { mutableStateOf(time?.minute?.let { "%02d".format(it) }) }

    // Update the LocalTime when both hour and minute are selected
    fun updateTime(hourStr: String?, minuteStr: String?) {
        val hour = hourStr?.toIntOrNull()
        val minute = minuteStr?.toIntOrNull()
        if (hour != null && minute != null) {
            onTimeChange(LocalTime.of(hour, minute))
        } else {
            onTimeChange(null)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 180.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "Now" button column
        Column(
            modifier = Modifier
                .weight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Current Date & Time", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            MediButton(onClick = {
                val nowDate = LocalDate.now()
                val nowTime = LocalTime.now()
                selectedHour.value = "%02d".format(nowTime.hour)
                selectedMinute.value = "%02d".format(nowTime.minute)
                onDateChange(nowDate)
                onTimeChange(nowTime)
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
            Text(text = "Manual Selection", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            MediButton(onClick = { dateSelectorState.value = true }) {
                Text(text = date?.toString() ?: "Select Date")
            }
        }

        // Time input column
        Column(
            modifier = Modifier
                .weight(0.8f)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            HourDropdown(
                currentHour = selectedHour.value,
                onHourChanged = { newHour ->
                    selectedHour.value = newHour
                    updateTime(newHour, selectedMinute.value)
                }
            )
            MinuteDropdown(
                currentMinute = selectedMinute.value,
                onMinuteChanged = { newMinute ->
                    selectedMinute.value = newMinute
                    updateTime(selectedHour.value, newMinute)
                }
            )
        }
    }

    // Show DateSelectorDialog when triggered
    if (dateSelectorState.value) {
        val context = LocalContext.current
        DateSelector(
            context = context,
            date = date,
            onDateSelected = { newDate ->
                onDateChange(newDate)
                dateSelectorState.value = false
            },
            onDismiss = {
                dateSelectorState.value = false
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DateTimeSelectorPreview() {
    DateTimeSelector(date = LocalDate.now(), time = LocalTime.now(), onDateChange = {}, onTimeChange = {})
}