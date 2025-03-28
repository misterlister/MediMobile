package com.example.medimobile.data.remote

import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.utils.DropdownConstants
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

// Deserializer for Patient Encounters
class PatientEncounterDeserializer(
    private val dropdownMappings: Map<String, List<DropdownItem>>
) : JsonDeserializer<PatientEncounter> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): PatientEncounter {
        val jsonObject = json.asJsonObject

        // Get equivalent display value from the dbvalue
        fun getMappedValue(field: String): String {
            val dbValue = jsonObject.get(field)?.asString ?: ""

            // Check if the dbValue starts with the OTHER_PREFIX, if so return it as is
            if (dbValue.startsWith(DropdownConstants.OTHER_PREFIX, ignoreCase = true)) {
                return dbValue
            }

            // Get the dropdown items for the given field
            val dropdownItems = dropdownMappings[field] ?: return "${DropdownConstants.OTHER_PREFIX}$dbValue"

            // Find the matching dropdown item, or else return "Other: $dbValue"
            return dropdownItems.find { it.dbValue.equals(dbValue, ignoreCase = true) }
                ?.displayValue
                ?: "${DropdownConstants.OTHER_PREFIX}$dbValue"
        }

        return PatientEncounter(
            age = jsonObject.get("age")?.asInt ?: 0,
            arrivalMethod = getMappedValue("arrival_method"),
            arrivalDate = context?.deserialize(jsonObject.get("arrival_date"), LocalDate::class.java),
            arrivalTime = context?.deserialize(jsonObject.get("arrival_time"), LocalTime::class.java),
            chiefComplaint = getMappedValue("chief_complaint"),
            comment = jsonObject.get("comment")?.asString ?: "",
            departureDate = context?.deserialize(jsonObject.get("departure_date"), LocalDate::class.java),
            departureTime = context?.deserialize(jsonObject.get("departure_time"), LocalTime::class.java),
            departureDest = getMappedValue("departure_dest"),
            documentNum = jsonObject.get("document_num")?.asString ?: "",
            location = jsonObject.get("location")?.asString ?: "",
            role = getMappedValue("role"),
            visitId = jsonObject.get("qr_code")?.asString ?: "",
            triageAcuity = jsonObject.get("triage_acuity")?.asString ?: "",
            dischargeDiagnosis = jsonObject.get("discharge_diagnosis")?.asString ?: "",
            dbKey = jsonObject.get("patient_encounter_uuid")?.asString ?: "",
            userUuid = jsonObject.get("user_uuid")?.asString ?: "",
            userEmail = jsonObject.get("user_email")?.asString ?: ""
        )
    }
}


// Deserializer for LocalDate
class LocalDateDeserializer : JsonDeserializer<LocalDate?> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate? {
        val dateTimeString = json?.asString
        return if (dateTimeString != null) {
            try {
                // Parse as LocalDateTime (since there's no timezone in the string)
                val localDateTime = LocalDateTime.parse(dateTimeString)

                // Convert to ZonedDateTime (with system default time zone) if needed
                val localDate = localDateTime.atZone(ZoneId.systemDefault()).toLocalDate()

                localDate
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
                val localDateTime = LocalDateTime.parse(dateTimeString)

                // Extract the time part from the LocalDateTime
                val localTime = localDateTime.toLocalTime()

                localTime
            } catch (e: Exception) {
                println("Error parsing time: $e")
                null
            }
        } else {
            null
        }
    }
}