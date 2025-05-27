package com.example.medimobile.data.remote

import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.utils.dbValueToDisplayValue
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

// Deserializer for Patient Encounters
class PatientEncounterDeserializer(
    private val dropdownMappings: Map<String, List<DropdownItem>>
) : JsonDeserializer<PatientEncounter> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): PatientEncounter {
        val jsonObject = json.asJsonObject

        fun field(fieldName: String) = jsonObject.get(fieldName)?.asString

        return PatientEncounter(
            age = jsonObject.get("age")?.takeIf { it.isJsonPrimitive }?.asInt,
            arrivalMethod = dbValueToDisplayValue(field("arrival_method"), "arrival_method", dropdownMappings),
            arrivalDate = context?.deserialize(jsonObject.get("arrival_date"), LocalDate::class.java),
            arrivalTime = context?.deserialize(jsonObject.get("arrival_time"), LocalTime::class.java),
            chiefComplaint = dbValueToDisplayValue(field("chief_complaint"), "chief_complaint", dropdownMappings),
            comment = field("comment") ?: "",
            departureDate = context?.deserialize(jsonObject.get("departure_date"), LocalDate::class.java),
            departureTime = context?.deserialize(jsonObject.get("departure_time"), LocalTime::class.java),
            departureDest = dbValueToDisplayValue(field("departure_dest"), "departure_dest", dropdownMappings),
            location = field("location") ?: "",
            event = field("event") ?: "",
            role = dbValueToDisplayValue(field("role"), "role", dropdownMappings),
            visitId = field("visit_id") ?: "",
            triageAcuity = field("triage_acuity") ?: "",
            dischargeDiagnosis = field("discharge_diagnosis") ?: "",
            encounterUuid = field("patient_encounter_uuid") ?: "",
            userUuid = field("user_uuid") ?: "",
            complete = jsonObject.get("complete")?.asBoolean ?: false
        )
    }
}


// Deserializer for LocalDate
class LocalDateDeserializer : JsonDeserializer<LocalDate?> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate? {
        val dateTimeString = json?.asString
        return if (dateTimeString != null) {
            try {
                // Parse the datetime string
                val utcDateTime = LocalDateTime.parse(dateTimeString)

                // Convert to ZonedDateTime with UTC
                val utcZonedDateTime = utcDateTime.atZone(ZoneOffset.UTC)

                // Convert UTC to system default
                val localZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault())

                // Get LocalDate in the local time zone
                localZonedDateTime.toLocalDate()
            } catch (e: Exception) {
                println("Error parsing date: $e")
                null
            }
        } else {
            null
        }
    }
}

// Deserializer for LocalTime
class LocalTimeDeserializer : JsonDeserializer<LocalTime?> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime? {
        val dateTimeString = json?.asString
        return if (dateTimeString != null) {
            try {
                // Parse as LocalDateTime
                val utcDateTime = LocalDateTime.parse(dateTimeString)

                // Convert to ZonedDateTime with UTC
                val utcZonedDateTime = utcDateTime.atZone(ZoneOffset.UTC)

                // Convert to system default time zone
                val localZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault())

                // Extract the time part in the local time zone
                localZonedDateTime.toLocalTime()
            } catch (e: Exception) {
                println("Error parsing time: $e")
                null
            }
        } else {
            null
        }
    }
}