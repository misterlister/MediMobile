package com.example.medimobile.ui.components.inputfields

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.components.dropdowns.HourDropdown
import com.example.medimobile.ui.components.dropdowns.MinuteDropdown
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun DateTimeSelector(
    date: LocalDate,
    time: LocalTime,
    onDateChange: (LocalDate) -> Unit,
    onTimeChange: (LocalTime) -> Unit
    ) {

    // Keeps track of whether the date picker is open
    val datePickerState = remember { mutableStateOf(false) }

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
            Text(text = "Select Date", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { datePickerState.value = true }) {
                Text(text = date.toString())
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
                currentHour = "%02d".format(time.hour), // Format the current hour as a string
                onHourChanged = { newHour ->
                    onTimeChange(time.withHour(newHour.toInt()))
                }
            )

            MinuteDropdown(
                currentMinute = "%02d".format(time.minute), // Format the current minute as a string
                onMinuteChanged = { newMinute ->
                    onTimeChange(time.withMinute(newMinute.toInt()))
                }
            )
        }
    }

    // Show DatePickerDialog when triggered
    if (datePickerState.value) {
        val context = LocalContext.current
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                onDateChange(newDate)
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        ).show()
        datePickerState.value = false // Close dialog after showing
    }
}