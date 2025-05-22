package com.example.medimobile.data.utils

import com.example.medimobile.data.constants.DropdownConstants.NOT_SET


fun isDataEmptyOrNull(value: Any?): Boolean {
    return when (value) {
        is String -> value.isEmpty() || value == NOT_SET // For String fields, check if empty/not set
        is Int -> value == 0 // For Int fields, check if zero (default value)
        else -> value == null // For any other type, check if null
    }
}