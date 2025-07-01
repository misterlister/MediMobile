package com.example.medimobile.data.utils

import com.example.medimobile.data.constants.DropdownConstants
import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.model.PatientEncounterFormData

fun mapToPatientEncounterFormData(
    encounter: PatientEncounter,
    dropdownMappings: Map<String, List<DropdownItem>>
): PatientEncounterFormData {

    val arrivalDateTimeUTC = convertToUTCDateTimeString(encounter.arrivalDate, encounter.arrivalTime)
    val departureDateTimeUTC = convertToUTCDateTimeString(encounter.departureDate, encounter.departureTime)

    return PatientEncounterFormData(
        age = encounter.age,
        arrivalMethod = displayValueToDbValue(encounter.arrivalMethod, "arrival_method", dropdownMappings),
        arrivalDateTime = arrivalDateTimeUTC,
        departureDateTime = departureDateTimeUTC,
        chiefComplaint = displayValueToDbValue(encounter.chiefComplaint, "chief_complaint", dropdownMappings),
        comment = encounter.comment,
        departureDest = displayValueToDbValue(encounter.departureDest, "departure_dest", dropdownMappings),
        location = encounter.location,
        event = encounter.event,
        role = displayValueToDbValue(encounter.role, "role", dropdownMappings),
        visitId = encounter.visitId,
        triageAcuity = encounter.triageAcuity,
        dischargeDiagnosis = encounter.dischargeDiagnosis,
        encounterUuid = encounter.encounterUuid,
        complete = encounter.complete
    )
}

// Get equivalent display value from the dbvalue
fun dbValueToDisplayValue(
    dbValue: String?,
    field: String,
    dropdownMappings: Map<String, List<DropdownItem>>
): String {
    if (dbValue.isNullOrBlank()) return ""

    if (dbValue.startsWith(DropdownConstants.OTHER_PREFIX, ignoreCase = true)) {
        return dbValue
    }

    val dropdownItems = dropdownMappings[field] ?: return "${DropdownConstants.OTHER_PREFIX}$dbValue"

    return dropdownItems.find { it.dbValue.equals(dbValue, ignoreCase = true) }
        ?.displayValue
        ?: "${DropdownConstants.OTHER_PREFIX}$dbValue"
}

// Get equivalent db value from the display value
fun displayValueToDbValue(
    displayValue: String,
    field: String,
    dropdownMappings: Map<String, List<DropdownItem>>
): String {
    if (isDataEmptyOrNull(displayValue)) return ""

    // If the display value starts with OTHER_PREFIX, return it as is
    if (displayValue.startsWith(DropdownConstants.OTHER_PREFIX, ignoreCase = true)) {
        return displayValue
    }

    // Otherwise, find the corresponding DB value from the dropdown mappings
    val dropdownItems = dropdownMappings[field]
    return dropdownItems?.find { it.displayValue.equals(displayValue, ignoreCase = true) }
        ?.dbValue
    // If no matching DB value is found, return the display value with the prefix
        ?: "${DropdownConstants.OTHER_PREFIX}$displayValue"
}
