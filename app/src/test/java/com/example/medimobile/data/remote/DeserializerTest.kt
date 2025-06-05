package com.example.medimobile.data.remote

import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.utils.convertUTCStringToLocalDateTime
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.TimeZone

class DeserializerTest {

    private lateinit var gson: Gson
    private lateinit var originalTimeZone: TimeZone

    private val dropdownMappings = mapOf(
        "arrival_method" to listOf(
            DropdownItem("self-presented", "Self Presented"),
            DropdownItem("med-transport", "Medical Transport"),
            DropdownItem("security", "Brought by Security"),
        ),
        "chief_complaint" to listOf(
            DropdownItem("allergic-reaction", "Allergic Reaction"),
            DropdownItem("bizarre-behaviour", "Bizarre Behaviour"),
            DropdownItem("shortness-of-breath", "Shortness of Breath"),
        ),
        "departure_dest" to listOf(
            DropdownItem("lwbs", "Left Without Being Seen"),
            DropdownItem("left-ama", "Left Against Medical Advice"),
            DropdownItem("hospital-netv", "Hospital via NETV"),
        ),
        "role" to listOf(
            DropdownItem("spectator", "Spectator"),
            DropdownItem("volunteer", "Volunteer"),
            DropdownItem("artist", "Artist"),
        )
    )

    val goodJSON = """{
        "age": 30,
        "arrival_method": "med-transport",
        "arrival_datetime": "2025-05-01T06:00:00",
        "chief_complaint": "allergic-reaction",
        "comment": "Patient feeling very unwell.",
        "departure_datetime": "2025-05-01T09:00:00",
        "departure_dest": "hospital-netv",
        "location": "Main Medical",
        "event": "Shambhala",
        "role": "spectator",
        "visit_id": "QSHAM0125-02345",
        "triage_acuity": "Red",
        "discharge_diagnosis": "Severe shellfish allergy",
        "patient_encounter_uuid": "b4b4b4b4b4b4",
        "user_uuid": "d5d5d5d5d5d5d5d5",
        "complete": true
    }""".trimIndent()

    val incompleteJSON = """{
        "arrival_method": "security",
        "chief_complaint": "bizarre-behaviour",
        "comment": "",
        "departure_dest": "left-ama",
        "location": "",
        "event": "",
        "role": "artist",
        "visit_id": "",
        "triage_acuity": "",
        "discharge_diagnosis": "",
        "patient_encounter_uuid": "",
        "user_uuid": "",
        "complete": false
    }""".trimIndent()

    val invalidDateJSON = """{
        "arrival_datetime": "not-a-date",
        "departure_datetime": null
    }""".trimIndent()

    val unusualContentJSON = """{
        "arrival_method": "teleportation",
        "chief_complaint": "unknown",
        "departure_dest": "the-void",
        "role": "extraterrestrial"
    }""".trimIndent()

    val nullContentJSON = """{
        "arrival_method": null,
        "chief_complaint": null,
        "departure_dest": null,
        "role": null
    }""".trimIndent()

    val emptyContentJSON = """{
        "arrival_method": "",
        "chief_complaint": "",
        "departure_dest": "",
        "role": ""
    }""".trimIndent()


    @Before
    fun setUp() {
        // Save original time zone and force PST for predictable results
        originalTimeZone = TimeZone.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("America/Vancouver"))

        gson = GsonBuilder()
            .registerTypeAdapter(PatientEncounter::class.java, PatientEncounterDeserializer(dropdownMappings))
            .create()
    }

    @After
    fun tearDown() {
        // Restore original time zone
        TimeZone.setDefault(originalTimeZone)
    }

    @Test
    fun `UTC datetime converts to correct LocalDateTime in local time zone`() {
        val utcString = "2025-05-01T06:00:00"
        val localDateTime = convertUTCStringToLocalDateTime(utcString)

        val expectedDateTime = LocalDateTime.of(2025, 4, 30, 23, 0)
        assertEquals(expectedDateTime, localDateTime)
    }

    @Test
    fun `invalid format returns null`() {
        val result = convertUTCStringToLocalDateTime("not-a-date")
        assertNull(result)
    }

    @Test
    fun `null input returns null`() {
        val result = convertUTCStringToLocalDateTime("null")
        assertNull(result)
    }

    @Test
    fun `valid JSON is parsed correctly`() {
        val encounter = gson.fromJson(goodJSON, PatientEncounter::class.java)
        assertEquals(30, encounter.age)
        assertEquals("Medical Transport", encounter.arrivalMethod)
        assertEquals("Allergic Reaction", encounter.chiefComplaint)
        assertEquals(LocalDate.of(2025, 4, 30), encounter.arrivalDate)
        assertTrue(encounter.complete)
    }

    @Test
    fun `incomplete JSON defaults correctly`() {
        val encounter = gson.fromJson(incompleteJSON, PatientEncounter::class.java)
        assertEquals("", encounter.comment)
        assertEquals("Brought by Security", encounter.arrivalMethod)
        assertFalse(encounter.complete)
        assertEquals("", encounter.location)
    }

    @Test
    fun `invalid date formats are handled safely`() {
        val encounter = gson.fromJson(invalidDateJSON, PatientEncounter::class.java)
        assertNull(encounter.arrivalDate)
        assertNull(encounter.arrivalTime)
        assertNull(encounter.departureDate)
        assertNull(encounter.departureTime)
    }

    @Test
    fun `unusual dropdown content is left unmapped or raw`() {
        val encounter = gson.fromJson(unusualContentJSON, PatientEncounter::class.java)
        assertEquals("other: teleportation", encounter.arrivalMethod)
        assertEquals("other: unknown", encounter.chiefComplaint)
        assertEquals("other: the-void", encounter.departureDest)
        assertEquals("other: extraterrestrial", encounter.role)
    }

    @Test
    fun `null content doesn't crash deserialization`() {
        val encounter = gson.fromJson(nullContentJSON, PatientEncounter::class.java)
        assertEquals("", encounter.arrivalMethod)
        assertEquals("", encounter.chiefComplaint)
        assertEquals("", encounter.departureDest)
        assertEquals("", encounter.role)
    }

    @Test
    fun `empty strings are preserved correctly`() {
        val encounter = gson.fromJson(emptyContentJSON, PatientEncounter::class.java)
        assertEquals("", encounter.arrivalMethod)
        assertEquals("", encounter.chiefComplaint)
    }
}

