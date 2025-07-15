package com.mgm.medimobile.data.utils

import com.mgm.medimobile.data.model.DropdownItem
import com.mgm.medimobile.data.model.MassGatheringEvent

fun List<DropdownItem>.toDisplayValues(): List<String> {
    return this.map { it.displayValue }
}

fun List<MassGatheringEvent>.toEventNames(): List<String> {
    return this.map { it.eventName }
}
