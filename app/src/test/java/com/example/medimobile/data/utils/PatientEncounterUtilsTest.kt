package com.example.medimobile.data.utils

import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.PatientEncounter
import org.junit.Test
import org.junit.Assert.assertEquals
import java.time.LocalDate
import java.time.LocalTime

class PatientEncounterUtilsTest {

    // **Mapping tests**
    @Test
    fun `map display values to db Values`() {
        val encounter = PatientEncounter(
            age = 25,
            arrivalMethod = "Self-Presented",
            chiefComplaint = "Headache",
            role = "Attendee",
            departureDest = "Left Against Medical Advice",
            arrivalDate = LocalDate.of(2025, 5, 1),
            arrivalTime = LocalTime.of(10, 30),
            departureDate = LocalDate.of(2025, 5, 2),
            departureTime = LocalTime.of(14, 45),
            comment = "No additional notes",
            location = "Main Medical",
            event = "Shambhala",
            visitId = "QSHAM0125-00123",
            triageAcuity = "Yellow",
            dischargeDiagnosis = "None",
            encounterUuid = "a1a1a1a1a1a1a1a",
            userUuid = "b2b2b2b2b2b2b2b2",
            complete = true
        )

        val mappings = mapOf(
            "arrival_method" to listOf(DropdownItem("self-presented", "Self-Presented")),
            "chief_complaint" to listOf(DropdownItem("headache", "Headache")),
            "role" to listOf(DropdownItem("attendee", "Attendee")),
            "departure_dest" to listOf(DropdownItem("left-ama", "Left Against Medical Advice"))
        )

        val result = mapToPatientEncounterFormData(encounter, mappings)

        assertEquals("self-presented", result.arrivalMethod)
        assertEquals("headache", result.chiefComplaint)
        assertEquals("attendee", result.role)
        assertEquals("left-ama", result.departureDest)
    }

    @Test
    fun `leaves 'other' values unchanged`() {
        val encounter = PatientEncounter(
            arrivalMethod = "other: Paraglider",
            chiefComplaint = "other: Brain fog",
            role = "other: Scribe",
            departureDest = "other: Submarine",
        )

        val mappings = mapOf(
            "arrival_method" to listOf(
                DropdownItem("not-a-paraglider", "Other: Paraglider"),
                DropdownItem("still-not-a-paraglider", "Paraglider")
            ),
            "chief_complaint" to listOf(
                DropdownItem("not-brain-fog", "Other: Brain fog"),
                DropdownItem("still-not-brain-fog", "Brain fog")
            ),
            "role" to listOf(
                DropdownItem("not-a-scribe", "Other: Scribe"),
                DropdownItem("still-not-a-scribe", "Scribe")
            ),
            "departure_dest" to listOf(
                DropdownItem("not-a-submarine", "Other: Submarine"),
                DropdownItem("still-not-a-submarine", "Submarine")
            )
        )

        val result = mapToPatientEncounterFormData(encounter, mappings)

        assertEquals("other: Paraglider", result.arrivalMethod)
        assertEquals("other: Brain fog", result.chiefComplaint)
        assertEquals("other: Scribe", result.role)
        assertEquals("other: Submarine", result.departureDest)
    }

    @Test
    fun `prefixes unknown values with OTHER_PREFIX`() {
        val encounter = PatientEncounter(
            arrivalMethod = "Teleportation",
            chiefComplaint = "Extremely Tired",
            role = "Nurse",
            departureDest = "Hotel"
        )

        val mappings = mapOf(
            "arrival_method" to listOf(DropdownItem("self-presented", "Self-Presented")),
        )

        val result = mapToPatientEncounterFormData(encounter, mappings)

        assertEquals("other: Teleportation", result.arrivalMethod)
        assertEquals("other: Extremely Tired", result.chiefComplaint)
        assertEquals("other: Nurse", result.role)
        assertEquals("other: Hotel", result.departureDest)
    }

    @Test
    fun `handles nulls and empty values safely`() {
        val encounter = PatientEncounter(
            arrivalMethod = "",
            chiefComplaint = "",
            role = "",
            departureDest = "",
        )

        val result = mapToPatientEncounterFormData(encounter, emptyMap())

        assertEquals("", result.arrivalMethod)
        assertEquals("", result.chiefComplaint)
        assertEquals("", result.role)
        assertEquals("", result.departureDest)
    }

    // **Tests for displayToDbValue**

    @Test
    fun `maps display to db correctly`() {
        val mappings = mapOf("role" to listOf(DropdownItem("scribe", "Scribe")))
        val result = displayValueToDbValue("Scribe", "role", mappings)
        assertEquals("scribe", result)
    }

    @Test
    fun `displayToDbValue returns other value unchanged`() {
        val result = displayValueToDbValue("other: Unicorn Rider", "role", emptyMap())
        assertEquals("other: Unicorn Rider", result)
    }

    @Test
    fun `displayToDbValue returns prefixed unknown`() {
        val mappings = mapOf("role" to listOf(DropdownItem("nurse", "Nurse")))
        val result = displayValueToDbValue("Astronaut", "role", mappings)
        assertEquals("other: Astronaut", result)
    }

    @Test
    fun `displayToDbValue returns empty for null or blank`() {
        assertEquals("", displayValueToDbValue("", "role", emptyMap()))
    }

    // **Tests for dbValueToDisplayValue**

    @Test
    fun `maps db to display correctly`() {
        val mappings = mapOf("arrival_method" to listOf(DropdownItem("teleport", "Teleportation")))
        val result = dbValueToDisplayValue("teleport", "arrival_method", mappings)
        assertEquals("Teleportation", result)
    }

    @Test
    fun `dbToDisplayValue returns other value unchanged`() {
        val result = dbValueToDisplayValue("other: Hoverboard", "arrival_method", emptyMap())
        assertEquals("other: Hoverboard", result)
    }

    @Test
    fun `dbToDisplayValue returns prefixed unknown`() {
        val mappings = mapOf("arrival_method" to listOf(DropdownItem("walking", "Walking")))
        val result = dbValueToDisplayValue("jetpack", "arrival_method", mappings)
        assertEquals("other: jetpack", result)
    }

    @Test
    fun `dbToDisplayValue returns empty for null or blank`() {
        assertEquals("", dbValueToDisplayValue(null, "arrival_method", emptyMap()))
        assertEquals("", dbValueToDisplayValue("", "arrival_method", emptyMap()))
    }
}
