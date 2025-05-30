package com.example.medimobile.testdata

import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.EventDropdowns
import com.example.medimobile.data.model.MassGatheringEvent
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.model.ServiceLocation
import java.time.LocalDate
import java.time.LocalTime

object TestData {
    val mockEvents: List<MassGatheringEvent> = listOf(
        MassGatheringEvent(
            eventName = "Event 1",
            locations = listOf(
                ServiceLocation("Location 1-1", "L11"),
                ServiceLocation("Location 1-2", "L12")
            ),
            eventID = "EV1",
            userGroups = listOf(
                DropdownItem("group1", "Group 1"),
                DropdownItem("group2", "Group 2")
            ),
            dropdowns = EventDropdowns(
                arrivalMethods = listOf(
                    DropdownItem("method1", "Method 1"),
                    DropdownItem("method2", "Method 2")
                ),
                departureDestinations = listOf(
                    DropdownItem("dest1", "Destination 1"),
                    DropdownItem("dest2", "Destination 2")
                ),
                roles = listOf(
                    DropdownItem("role1", "Role 1"),
                    DropdownItem("role2", "Role 2")
                ),
                chiefComplaints = listOf(
                    DropdownItem("complaint1", "Complaint 1"),
                    DropdownItem("complaint2", "Complaint 2")
                )
            )
        ),
        MassGatheringEvent(
            eventName = "Event 2",
            locations = listOf(
                ServiceLocation("Location 2-1", "L21"),
                ServiceLocation("Location 2-2", "L22")
            ),
            eventID = "EV2",
            userGroups = listOf(
                DropdownItem("group3", "Group 3"),
                DropdownItem("group4", "Group 4")
            ),
            dropdowns = EventDropdowns(
                arrivalMethods = listOf(
                    DropdownItem("method3", "Method 3"),
                    DropdownItem("method4", "Method 4")
                ),
                departureDestinations = listOf(
                    DropdownItem("dest3", "Destination 3"),
                    DropdownItem("dest4", "Destination 4")
                ),
                roles = listOf(
                    DropdownItem("role3", "Role 3"),
                    DropdownItem("role4", "Role 4")
                ),
                chiefComplaints = listOf(
                    DropdownItem("complaint3", "Complaint 3"),
                    DropdownItem("complaint4", "Complaint 4")
                )
            )
        )
    )

    val mockEncounters: List<PatientEncounter> = listOf(
        // 1. Minimal data
        PatientEncounter(
            age = 0,
            arrivalMethod = "",
            chiefComplaint = "",
            role = "",
            departureDest = "",
            arrivalDate = LocalDate.MIN,
            arrivalTime = LocalTime.MIDNIGHT,
            departureDate = LocalDate.MIN,
            departureTime = LocalTime.MIDNIGHT,
            comment = "",
            location = "",
            event = "",
            visitId = "",
            triageAcuity = "",
            dischargeDiagnosis = "",
            encounterUuid = "incomplete1",
            userUuid = "userIncomplete1",
            complete = false
        ),

        // 2. Partial data
        PatientEncounter(
            age = 22,
            arrivalMethod = "Method 1",
            chiefComplaint = "Complaint 1",
            role = "",
            departureDest = "",
            arrivalDate = LocalDate.of(2025, 6, 1),
            arrivalTime = LocalTime.of(9, 0),
            departureDate = LocalDate.MIN,
            departureTime = LocalTime.MIDNIGHT,
            comment = "",
            location = "Location 1-1",
            event = "Event 1",
            visitId = "GEV1L1125-00001",
            triageAcuity = "",
            dischargeDiagnosis = "",
            encounterUuid = "partial1",
            userUuid = "userPartial1",
            complete = false
        ),

        // 3. Mostly complete
        PatientEncounter(
            age = 30,
            arrivalMethod = "Method 2",
            chiefComplaint = "Complaint 2",
            role = "Role 2",
            departureDest = "Destination 2",
            arrivalDate = LocalDate.of(2025, 6, 2),
            arrivalTime = LocalTime.of(11, 45),
            departureDate = LocalDate.of(2025, 6, 2),
            departureTime = LocalTime.of(15, 0),
            comment = "Mild headache reported",
            location = "Location 1-2",
            event = "Event 1",
            visitId = "QEV1L1225-00002",
            triageAcuity = "Yellow",
            dischargeDiagnosis = "",
            encounterUuid = "almostComplete1",
            userUuid = "userAlmostComplete1",
            complete = true
        ),

        // 4. Fully complete
        PatientEncounter(
            age = 40,
            arrivalMethod = "Method 3",
            chiefComplaint = "Complaint 3",
            role = "Role 3",
            departureDest = "Destination 3",
            arrivalDate = LocalDate.of(2025, 6, 3),
            arrivalTime = LocalTime.of(13, 0),
            departureDate = LocalDate.of(2025, 6, 3),
            departureTime = LocalTime.of(13, 45),
            comment = "Treated for sunburn and dehydration",
            location = "Location 2-1",
            event = "Event 2",
            visitId = "GEV2L2125-00001",
            triageAcuity = "Green",
            dischargeDiagnosis = "Sunburn, Dehydration",
            encounterUuid = "complete1",
            userUuid = "userComplete1",
            complete = true
        ),

        // 5. Fully complete
        PatientEncounter(
            age = 35,
            arrivalMethod = "Method 4",
            chiefComplaint = "Complaint 4",
            role = "Role 4",
            departureDest = "Destination 4",
            arrivalDate = LocalDate.of(2025, 6, 4),
            arrivalTime = LocalTime.of(10, 20),
            departureDate = LocalDate.of(2025, 6, 4),
            departureTime = LocalTime.of(11, 30),
            comment = "Headache and fatigue, no further complications",
            location = "Location 2-2",
            event = "Event 2",
            visitId = "QEV2L2225-00002",
            triageAcuity = "Yellow",
            dischargeDiagnosis = "Fatigue",
            encounterUuid = "complete2",
            userUuid = "userComplete2",
            complete = true
        )
    )

}