package com.example.medimobile.data.utils

import com.example.medimobile.data.model.PatientEncounter
import java.time.format.DateTimeFormatter

// Formatter for HH:mm
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

// Formatter for dd/MM/yy
private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

// Formatter for dd/MM/yy-HH:mm
fun formatArrivalDateTime(encounter: PatientEncounter): String {
    return "${encounter.arrivalTime.format(timeFormatter)} ${encounter.arrivalDate.format(dateFormatter)}"
}