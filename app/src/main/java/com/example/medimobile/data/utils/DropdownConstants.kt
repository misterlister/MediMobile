package com.example.medimobile.data.utils

object DropdownConstants {
    val HOURS = (0..23).map { "%02d".format(it) }

    val MINUTES = (0..59).map { "%02d".format(it) }

    val TRIAGE_LEVELS = listOf(
        "Green",
        "Yellow",
        "Red",
        "White"
    )

    const val OTHER_PREFIX = "other: "
}