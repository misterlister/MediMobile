package com.example.medimobile.data.model

data class MassGatheringEvent(
    val eventName: String,
    val locations: List<DropdownItem>,
    val dropdowns: EventDropdowns,
)
