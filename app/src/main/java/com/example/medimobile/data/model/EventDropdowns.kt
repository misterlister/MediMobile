package com.example.medimobile.data.model

data class EventDropdowns(
    val arrivalMethods: List<DropdownItem>,
    val departureDestinations: List<DropdownItem>,
    val roles: List<DropdownItem>,
    val chiefComplaints: List<DropdownItem>
)
