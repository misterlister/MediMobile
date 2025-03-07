package com.example.medimobile.data.utils

import com.example.medimobile.data.model.DropdownItem

object ShambhalaDropdowns {
    val arrivalMethods =  listOf(
            DropdownItem("self-presented", "Self Presented"),
            DropdownItem("med-transport", "Medical Transport"),
            DropdownItem("security", "Brought by Security"),
            DropdownItem("harm-reduction", "Brought by Harm Reduction"),
            DropdownItem("Other", "Other")
        )

    val departureDestinations = listOf(
            DropdownItem("lwbs", "Left Without Being Seen"),
            DropdownItem("left-ama", "Left Against Medical Advice"),
            DropdownItem("return-to-event", "Returned to event"),
            DropdownItem("security", "Security"),
            DropdownItem("sanctuary", "Sanctuary"),
            DropdownItem("hospital-ambulance", "Hospital via Ambulance"),
            DropdownItem("hospital-netv", "Hospital via NETV"),
            DropdownItem("hospital-own", "Hospital via Own"),
            DropdownItem("Other", "Other")
        )

    val triageLevels = listOf(
            DropdownItem("Red", "Red"),
            DropdownItem("Yellow", "Yellow"),
            DropdownItem("Green", "Green"),
            DropdownItem("White", "White")
        )

    val roles = listOf(
            DropdownItem("Spectator", "Spectator"),
            DropdownItem("Volunteer", "Volunteer"),
            DropdownItem("Staff", "Staff"),
            DropdownItem("Vendor", "Vendor"),
            DropdownItem("Artist", "Artist"),
            DropdownItem("Other", "Other")
        )

    val chiefComplaints = listOf(
            DropdownItem("Agitation", "Agitation"),
            DropdownItem("Allergic Reaction", "Allergic Reaction"),
            DropdownItem("Anxiety", "Anxiety"),
            DropdownItem("Bizarre Behaviour", "Bizarre Behaviour"),
            DropdownItem("Diarrhea", "Diarrhea"),
            DropdownItem("Dizziness / Presyncope / Lightheaded", "Dizziness / Presyncope / Lightheaded"),
            DropdownItem("Decreased Level of / Loss of Consciousness", "Decreased Level of / Loss of Consciousness"),
            DropdownItem("Eye Ear Nose", "Eye Ear Nose"),
            DropdownItem("Mental Health Concerns / Depression / Suicidal", "Mental Health Concerns / Depression / Suicidal"),
            DropdownItem("Nausea and/or Vomiting", "Nausea and/or Vomiting"),
            DropdownItem("Pain - Abdo", "Pain - Abdo"),
            DropdownItem("Pain - Back", "Pain - Back"),
            DropdownItem("Pain - Chest", "Pain - Chest"),
            DropdownItem("Pain - Headache", "Pain - Headache"),
            DropdownItem("Pain - Other", "Pain - Other"),
            DropdownItem("Prescription Request", "Prescription Request"),
            DropdownItem("Seizure", "Seizure"),
            DropdownItem("Shortness of Breath", "Shortness of Breath"),
            DropdownItem("Trauma", "Trauma"),
            DropdownItem("Urinary Concerns", "Urinary Concerns"),
            DropdownItem("Other", "Other")
        )
}