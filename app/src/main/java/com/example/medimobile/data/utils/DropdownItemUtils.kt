package com.example.medimobile.data.utils

import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.MassGatheringEvent

fun List<DropdownItem>.toDisplayValues(): List<String> {
    return this.map { it.displayValue }
}

fun List<MassGatheringEvent>.toEventNames(): List<String> {
    return this.map { it.eventName }
}
