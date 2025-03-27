package com.example.medimobile.data.utils

import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.utils.DropdownConstants.NOT_SET
import java.time.format.DateTimeFormatter

// Formatter for HH:mm
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

// Formatter for dd/MM/yy
private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

// Formatter for dd/MM/yy-HH:mm
fun formatArrivalDateTime(encounter: PatientEncounter): String {
    // Handle null values for date and time with "Not Set"
    val datePart = encounter.arrivalDate?.format(dateFormatter) ?: NOT_SET
    val timePart = encounter.arrivalTime?.format(timeFormatter) ?: NOT_SET

    return "$timePart $datePart"
}