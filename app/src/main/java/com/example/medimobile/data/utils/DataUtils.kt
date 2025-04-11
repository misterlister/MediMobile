package com.example.medimobile.data.utils


fun isDataEmptyOrNull(value: Any?): Boolean {
    return when (value) {
        is String -> value.isEmpty() // For String fields, check if empty
        is Int -> value == 0 // For Int fields, check if zero (default value)
        else -> value == null // For any other type, check if null
    }
}