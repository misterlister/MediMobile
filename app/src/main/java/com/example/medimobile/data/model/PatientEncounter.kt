package com.example.medimobile.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.medimobile.ui.theme.LocalExtendedColors
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

data class PatientEncounter (
    val age: Int? = null,
    @SerializedName("arrival_method") val arrivalMethod: String = "",
    @SerializedName("arrival_date") val arrivalDate: LocalDate? = null,
    @SerializedName("arrival_time") val arrivalTime: LocalTime? = null,
    @SerializedName("chief_complaint") val chiefComplaint: String = "",
    val comment: String = "",
    @SerializedName("departure_date") val departureDate: LocalDate? = null,
    @SerializedName("departure_time") val departureTime: LocalTime? = null,
    @SerializedName("departure_dest") val departureDest: String = "",
    val location: String = "",
    val event: String = "",
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
    val submitted: Boolean = false
)

enum class StageStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETE
}

@Composable
fun getStatusColour(stageStatus: StageStatus?): Color {
    val extendedColors = LocalExtendedColors.current
    return when (stageStatus) {
        StageStatus.COMPLETE -> extendedColors.medigreen.colorContainer
        StageStatus.IN_PROGRESS -> extendedColors.mediyellow.colorContainer
        StageStatus.NOT_STARTED -> extendedColors.medigrey.colorContainer
        else -> extendedColors.medigrey.colorContainer
    }
}

@Composable
fun getStatusTextColour(stageStatus: StageStatus?): Color {
    val extendedColors = LocalExtendedColors.current
    return when (stageStatus) {
        StageStatus.COMPLETE -> extendedColors.medigreen.onColorContainer
        StageStatus.IN_PROGRESS -> extendedColors.mediyellow.onColorContainer
        StageStatus.NOT_STARTED -> extendedColors.medigrey.onColorContainer
        else -> extendedColors.medigrey.onColorContainer
    }
}

data class PatientEncounterFormData(
    val age: Int?,
    @SerializedName("arrival_method") val arrivalMethod: String,
    @SerializedName("arrival_datetime") val arrivalDateTime: String?,
    @SerializedName("chief_complaint") val chiefComplaint: String,
    val comment: String,
    @SerializedName("departure_datetime") val departureDateTime: String?,
    @SerializedName("departure_dest") val departureDest: String,
    @SerializedName("visit_id") val visitId: String,
    val location: String,
    val event: String,
    val role: String,
    @SerializedName("triage_acuity") val triageAcuity: String,
    @SerializedName("discharge_diagnosis") val dischargeDiagnosis: String,
    @SerializedName("patient_encounter_uuid") val encounterUuid: String? = null,
    val complete: Boolean
)
