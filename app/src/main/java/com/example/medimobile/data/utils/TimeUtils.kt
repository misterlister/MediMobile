package com.example.medimobile.data.utils

import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.utils.DropdownConstants.NOT_SET
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Formatter for HH:mm
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

// Formatter for dd/MM/yy
val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

// Formatter for HH:mm - dd/MM/yy
fun formatArrivalDateTime(encounter: PatientEncounter): String {
    // Handle null values for date and time with "Not Set"
    val datePart = encounter.arrivalDate?.format(dateFormatter) ?: NOT_SET
    val timePart = encounter.arrivalTime?.format(timeFormatter) ?: NOT_SET

    return "$timePart - $datePart"
}

// Convert local date and time to UTC string
fun convertToUTCString(date: LocalDate?, time: LocalTime?): String {
    if (date == null || time == null) return "" // Handle missing values if needed
    val zoneId = ZoneId.systemDefault() // Adjust if needed
    val dateTime = LocalDateTime.of(date, time)
    val instant = dateTime.atZone(zoneId).toInstant()
    return DateTimeFormatter.ISO_INSTANT.format(instant) // Format as "2025-04-01T23:00:08.273Z"
}