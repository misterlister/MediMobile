package com.example.medimobile.data.utils

import com.example.medimobile.data.model.DropdownItem

object DropdownConstants {
    val HOURS = (0..23).map {
        DropdownItem("%02d".format(it), "%02d".format(it))  // dbValue and displayValue are the same for hours
    }

    val MINUTES = (0..59).map {
        DropdownItem("%02d".format(it), "%02d".format(it))  // dbValue and displayValue are the same for minutes
    }
}