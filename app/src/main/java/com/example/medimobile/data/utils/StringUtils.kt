package com.example.medimobile.data.utils

import com.example.medimobile.data.constants.IDConstants.VISIT_SUFFIX_LEN
import com.example.medimobile.data.constants.IDConstants.VisitIDCategory
import java.time.LocalDate


// Convert enum to display value
fun DateRangeOption.toDisplayValue(): String {
    return when (this) {
        DateRangeOption.DAY -> "Day"
        DateRangeOption.WEEK -> "Week"
        DateRangeOption.MONTH -> "Month"
        DateRangeOption.YEAR -> "Year"
        DateRangeOption.ALL_TIME -> "All Time"
    }
}

// Convert display value back to enum
fun String.toDateRangeOption(): DateRangeOption {
    return when (this.lowercase()) {
        "day" -> DateRangeOption.DAY
        "week" -> DateRangeOption.WEEK
        "month" -> DateRangeOption.MONTH
        "year" -> DateRangeOption.YEAR
        "all time" -> DateRangeOption.ALL_TIME
        else -> throw IllegalArgumentException("Unknown DateRangeOption")
    }
}

// Join the parts of the visit ID together, and format them accordingly
fun formatVisitID(category: VisitIDCategory, eventID: String, locationID: String, date: LocalDate, visitSuffix: Int): String {
    val visitCategory = when (category) {
        VisitIDCategory.QR_CODE -> "Q"
        VisitIDCategory.GENERATED -> "G"
    }

    val paddedVisitSuffix = visitSuffix.toString().padStart(VISIT_SUFFIX_LEN, '0')
    val year = getUTCYearSuffix(date) ?: "??" // This will prevent errors from blocking submission

    return "$visitCategory$eventID$locationID$year-$paddedVisitSuffix"
}

fun Enum<*>.toText(): String {
    return name.lowercase()
        .split('_')
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
}