package com.example.medimobile.data.utils

fun String.toRegularCase(): String {
    return this.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
}

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