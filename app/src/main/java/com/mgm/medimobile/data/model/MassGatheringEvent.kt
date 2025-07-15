package com.mgm.medimobile.data.model

data class MassGatheringEvent(
    val eventName: String,
    val eventID: String,
    val locations: List<ServiceLocation>,
    val userGroups: List<DropdownItem>,
    val dropdowns: EventDropdowns
)
