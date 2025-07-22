package com.mgm.medimobile.data.constants

object DropdownConstants {
    val HOURS = (0..23).map { "%02d".format(it) }

    val MINUTES = (0..59).map { "%02d".format(it) }

    const val NOT_SET = "--"

    val TRIAGE_LEVELS = listOf(
        "Green",
        "Yellow",
        "Red",
        // "White" // Removed as per request from team
    )

    const val OTHER_PREFIX = "other: "

    const val NO_OPTIONS = "No options available"

    const val DROPDOWN_WIDTH = 200
    const val DROPDOWN_HEIGHT = 425
}