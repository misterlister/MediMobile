package com.example.medimobile.data.model

data class ServiceLocation (
    // The name of the location/service
    val locationName: String,
    // The 3-character ID of the location/service
    val locationID: String,
    // A description of the location/service (optional)
    val description: String = ""
)