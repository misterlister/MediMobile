package com.example.medimobile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HourDropdown(
    hour: Int,
    onHourChanged: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val hours = (0..23).toList()

    Column {
        Text(text = "Hour", fontWeight = FontWeight.Bold) // Label
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
                .clickable { expanded = true }
        ) {
            TextField(
                value = "%02d".format(hour), // Format the hour with leading zero
                onValueChange = {},
                label = { Text("Hour") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                hours.forEach { hourOption ->
                    DropdownMenuItem(
                        text = { Text("%02d".format(hourOption)) },
                        onClick = {
                        onHourChanged(hourOption)
                        expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MinuteDropdown(
    minute: Int,
    onMinuteChanged: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val minutes = (0..59).toList()

    Column {
        Text(text = "Minute", fontWeight = FontWeight.Bold) // Label
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
                .clickable { expanded = true }
        ) {
            TextField(
                value = "%02d".format(minute), // Format the minute with leading zero
                onValueChange = {},
                label = { Text("Minute") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                minutes.forEach { minuteOption ->
                    DropdownMenuItem(
                        text = { Text("%02d".format(minuteOption)) },
                        onClick = {
                            onMinuteChanged(minuteOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
