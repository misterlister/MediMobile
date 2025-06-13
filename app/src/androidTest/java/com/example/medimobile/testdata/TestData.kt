package com.example.medimobile.testdata

import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.EventDropdowns
import com.example.medimobile.data.model.MassGatheringEvent
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.model.ServiceLocation
import java.time.LocalDate
import java.time.LocalTime

object TestData {
    const val EVENT_NAME_1 = "Event 1"
    const val EVENT_NAME_2 = "Event 2"

    const val GROUP_1_EVENT_1 = "Group 1"
    const val GROUP_2_EVENT_1 = "Group 2"
    const val GROUP_1_EVENT_2 = "Group 3"
    const val GROUP_2_EVENT_2 = "Group 4"

    const val LOCATION_1_1 = "Location 1-1"
    const val LOCATION_1_2 = "Location 1-2"
    const val LOCATION_2_1 = "Location 2-1"
    const val LOCATION_2_2 = "Location 2-2"

    val mockEvents: List<MassGatheringEvent> = listOf(
        MassGatheringEvent(
            eventName = EVENT_NAME_1,
            locations = listOf(
                ServiceLocation(LOCATION_1_1, "L11"),
                ServiceLocation(LOCATION_1_2, "L12")
            ),
            eventID = "EV1",
            userGroups = listOf(
                DropdownItem("group1", GROUP_1_EVENT_1),
                DropdownItem("group2", GROUP_2_EVENT_1)
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
            eventName = EVENT_NAME_2,
            locations = listOf(
                ServiceLocation(LOCATION_2_1, "L21"),
                ServiceLocation(LOCATION_2_2, "L22")
            ),
            eventID = "EV2",
            userGroups = listOf(
                DropdownItem("group3", GROUP_1_EVENT_2),
                DropdownItem("group4", GROUP_2_EVENT_2)
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

    const val USERNAME_1 = "testuser"
    const val USERNAME_2 = "testuser2"
    const val VALID_PASSWORD = "password123"

    const val INVALID_USERNAME = "invaliduser"
    const val INVALID_PASSWORD = "invalidpassword"
    const val INVALID_GROUP = "Invalid Group"

    const val SUCCESS_TOKEN = "SuccessToken"
    const val INVALID_CREDENTIALS_TOKEN = "Invalid credentials"

    const val FAKE_VISIT_ID = "GFAKVIS25-11111"
    const val FAKE_AGE = "25"

    const val FAKE_COMMENT = "This is a fake comment."
    const val FAKE_DIAGNOSIS = "This is a fake diagnosis."
}