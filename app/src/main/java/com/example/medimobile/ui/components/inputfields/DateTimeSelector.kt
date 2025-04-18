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
import androidx.compose.material3.Button
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
import com.example.medimobile.data.utils.DropdownConstants.NOT_SET
import com.example.medimobile.ui.components.dropdowns.HourDropdown
import com.example.medimobile.ui.components.dropdowns.MinuteDropdown
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
            Button(onClick = { dateSelectorState.value = true }) {
                Text(text = date?.toString() ?: NOT_SET)
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
                currentHour = "%02d".format(time?.hour ?: 0), // Format the current hour as a string
                onHourChanged = { newHour ->
                    onTimeChange(time?.withHour(newHour.toInt()))
                }
            )

            MinuteDropdown(
                currentMinute = "%02d".format(time?.minute ?: 0), // Format the current minute as a string
                onMinuteChanged = { newMinute ->
                    onTimeChange(time?.withMinute(newMinute.toInt()))
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
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DateTimeSelectorPreview() {
    DateTimeSelector(date = LocalDate.now(), time = LocalTime.now(), onDateChange = {}, onTimeChange = {})
}