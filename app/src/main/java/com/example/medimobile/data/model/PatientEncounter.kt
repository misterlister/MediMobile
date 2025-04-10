package com.example.medimobile.data.model

import com.example.medimobile.data.utils.convertToUTCDateString
import com.example.medimobile.data.utils.convertToUTCDateTimeString
import com.example.medimobile.data.utils.convertToUTCString
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

data class PatientEncounter (
    val age: Int = 0,
    @SerializedName("arrival_method") val arrivalMethod: String = "",
    @SerializedName("arrival_date") val arrivalDate: LocalDate? = null,
    @SerializedName("arrival_time") val arrivalTime: LocalTime? = null,
    @SerializedName("chief_complaint") val chiefComplaint: String = "",
    val comment: String = "",
    @SerializedName("departure_date") val departureDate: LocalDate? = null,
    @SerializedName("departure_time") val departureTime: LocalTime? = null,
    @SerializedName("departure_dest") val departureDest: String = "",
    val location: String = "",
    val role: String = "",
    @SerializedName("visit_id") val visitId: String = "",
    @SerializedName("triage_acuity") val triageAcuity: String = "",
    @SerializedName("discharge_diagnosis") val dischargeDiagnosis: String = "",
    @SerializedName("patient_encounter_uuid") val encounterUuid: String? = null,
    @SerializedName("user_uuid") val userUuid: String? = null,
    val triageStatus: StageStatus = StageStatus.NOT_STARTED,
    val informationCollectionStatus: StageStatus = StageStatus.NOT_STARTED,
    val dischargeStatus: StageStatus = StageStatus.NOT_STARTED,
    val complete: Boolean = false,

)

enum class StageStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETE
}

data class PatientEncounterFormData(
    val age: Int,
    @SerializedName("arrival_method") val arrivalMethod: String,
    @SerializedName("arrival_date") val arrivalDate: String?,
    @SerializedName("arrival_time") val arrivalTime: String?,
    @SerializedName("chief_complaint") val chiefComplaint: String,
    val comment: String,
    @SerializedName("departure_date") val departureDate: String?,
    @SerializedName("departure_time") val departureTime: String?,
    @SerializedName("departure_dest") val departureDest: String,
    @SerializedName("visit_id") val visitId: String,
    val location: String,
    val role: String,
    @SerializedName("triage_acuity") val triageAcuity: String,
    @SerializedName("discharge_diagnosis") val dischargeDiagnosis: String,
    @SerializedName("patient_encounter_uuid") val encounterUuid: String? = null,
)

fun mapToPatientEncounterFormData(encounter: PatientEncounter): PatientEncounterFormData {
    val arrivalDateUTC = convertToUTCDateString(encounter.arrivalDate)
    val arrivalTimeUTC = convertToUTCDateTimeString(encounter.arrivalDate, encounter.arrivalTime)

    val departureDateUTC = convertToUTCDateString(encounter.departureDate)
    val departureTimeUTC = convertToUTCDateTimeString(encounter.departureDate, encounter.departureTime)

    return PatientEncounterFormData(
        age = encounter.age,
        arrivalMethod = encounter.arrivalMethod,
        arrivalDate = arrivalDateUTC,
        arrivalTime = arrivalTimeUTC,
        departureDate = departureDateUTC,
        departureTime = departureTimeUTC,
        chiefComplaint = encounter.chiefComplaint,
        comment = encounter.comment,
        departureDest = encounter.departureDest,
        location = encounter.location,
        role = encounter.role,
        visitId = encounter.visitId,
        triageAcuity = encounter.triageAcuity,
        dischargeDiagnosis = encounter.dischargeDiagnosis,
        encounterUuid = encounter.encounterUuid,
    )
}