package com.example.medimobile.data.model

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
    @SerializedName("patient_encounter_uuid") val dbKey: String = "",
    val triageStatus: StageStatus = StageStatus.NOT_STARTED,
    val informationCollectionStatus: StageStatus = StageStatus.NOT_STARTED,
    val dischargeStatus: StageStatus = StageStatus.NOT_STARTED,
    val complete: Boolean = false,
    @SerializedName("user_uuid") val userUuid: String = "",
    @SerializedName("user_email") val userEmail: String = ""
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
    @SerializedName("user_uuid") val userUuid: String? = null,
    @SerializedName("user_email") val userEmail: String
)

fun mapToPatientEncounterFormData(encounter: PatientEncounter): PatientEncounterFormData {
    val arrivalUTC = convertToUTCString(encounter.arrivalDate, encounter.arrivalTime)
    val departureUTC = convertToUTCString(encounter.departureDate, encounter.departureTime)

    return PatientEncounterFormData(
        age = encounter.age,
        arrivalMethod = encounter.arrivalMethod,
        arrivalDate = arrivalUTC,
        arrivalTime = arrivalUTC,
        departureDate = departureUTC,
        departureTime = departureUTC,
        chiefComplaint = encounter.chiefComplaint,
        comment = encounter.comment,
        departureDest = encounter.departureDest,
        location = encounter.location,
        role = encounter.role,
        visitId = encounter.visitId,
        triageAcuity = encounter.triageAcuity,
        dischargeDiagnosis = encounter.dischargeDiagnosis,
        userEmail = encounter.userEmail
    )
}