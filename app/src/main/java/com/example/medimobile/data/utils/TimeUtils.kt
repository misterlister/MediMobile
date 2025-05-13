package com.example.medimobile.data.utils

import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.constants.DropdownConstants.NOT_SET
import com.example.medimobile.data.constants.IDConstants.YEAR_LEN
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

// Formatter for HH:mm
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

// Formatter for dd/MM/yy
val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

// Formatter for dd/MM/yy - HH:mm
fun formatArrivalDateTime(encounter: PatientEncounter): String {
    // Handle null values for date and time with "Not Set"
    val datePart = encounter.arrivalDate?.format(dateFormatter) ?: NOT_SET
    val timePart = encounter.arrivalTime?.format(timeFormatter) ?: NOT_SET
    if (datePart == NOT_SET && timePart == NOT_SET) {
        return NOT_SET
    }

    return "$datePart - $timePart"
}

fun convertToUTCDateString(date: LocalDate?): String? {
    return date?.atStartOfDay(ZoneId.systemDefault())
        ?.withZoneSameInstant(ZoneOffset.UTC)
        ?.toLocalDateTime()
        ?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}

fun convertToUTCDateTimeString(date: LocalDate?, time: LocalTime?): String? {
    if (date == null || time == null) return null
    return LocalDateTime.of(date, time)
        .atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime()
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}

fun getUTCYearSuffix(date: LocalDate?): String? {
    return date?.atStartOfDay(ZoneId.systemDefault())
        ?.withZoneSameInstant(ZoneOffset.UTC)
        ?.toLocalDate()
        ?.year
        ?.toString()
        ?.takeLast(YEAR_LEN)
}

enum class DateRangeOption {
    DAY, WEEK, MONTH, YEAR, ALL_TIME
}

