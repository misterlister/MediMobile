package com.mgm.medimobile.data.remote

import com.mgm.medimobile.data.model.DropdownItem
import com.mgm.medimobile.data.model.PatientEncounter
import com.mgm.medimobile.data.utils.convertUTCStringToLocalDateTime
import com.mgm.medimobile.data.utils.dbValueToDisplayValue
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import java.lang.reflect.Type
import java.time.LocalDateTime

// Deserializer for Patient Encounters
class PatientEncounterDeserializer(
    private val dropdownMappings: Map<String, List<DropdownItem>>
) : JsonDeserializer<PatientEncounter> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): PatientEncounter {
        val jsonObject = json.asJsonObject

        // Get field content for a non-nullable field
        fun field(fieldName: String): String =
            jsonObject.get(fieldName)
                ?.takeIf { it !is JsonNull }
                ?.asString
                ?: ""

        // Get field content for a nullable field
        fun nullableField(fieldName: String): String? =
            jsonObject.get(fieldName)
                ?.takeIf { it !is JsonNull }
                ?.asString

        // Get field content for a nullable int
        fun nullableIntField(fieldName: String): Int? =
            jsonObject.get(fieldName)
                ?.takeIf { it.isJsonPrimitive && it !is JsonNull }
                ?.asInt

        // Get field content for a nullable LocalDateTime
        fun nullableLocalDateTimeField(fieldName: String): LocalDateTime? {
            val dateTimeString = jsonObject.get(fieldName)?.takeIf { it !is JsonNull }?.asString ?: return null
            return convertUTCStringToLocalDateTime(dateTimeString)
        }

        val arrivalDateTime = nullableLocalDateTimeField("arrival_datetime")
        val departureDateTime = nullableLocalDateTimeField("departure_datetime")

        return PatientEncounter(
            age = nullableIntField("age"),
            arrivalMethod = dbValueToDisplayValue(field("arrival_method"), "arrival_method", dropdownMappings),
            arrivalDate = arrivalDateTime?.toLocalDate(),
            arrivalTime = arrivalDateTime?.toLocalTime(),
            chiefComplaint = dbValueToDisplayValue(field("chief_complaint"), "chief_complaint", dropdownMappings),
            comment = field("comment"),
            departureDate = departureDateTime?.toLocalDate(),
            departureTime = departureDateTime?.toLocalTime(),
            departureDest = dbValueToDisplayValue(field("departure_dest"), "departure_dest", dropdownMappings),
            location = field("location"),
            event = field("event"),
            role = dbValueToDisplayValue(field("role"), "role", dropdownMappings),
            visitId = field("visit_id"),
            triageAcuity = field("triage_acuity"),
            dischargeDiagnosis = field("discharge_diagnosis"),
            encounterUuid = nullableField("patient_encounter_uuid"),
            userUuid = nullableField("user_uuid"),
            complete = jsonObject.get("complete")?.asBoolean ?: false
        )
    }
}
