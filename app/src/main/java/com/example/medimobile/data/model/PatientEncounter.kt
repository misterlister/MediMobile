package com.example.medimobile.data.model

import java.time.LocalDate
import java.time.LocalTime

data class PatientEncounter (
    val age: Int = 0,
    val arrivalMethod: String = "",
    val arrivalDate: LocalDate = LocalDate.now(),
    val arrivalTime: LocalTime = LocalTime.now(),
    val chiefComplaint: String = "",
    val comment: String = "",
    val departureDate: LocalDate = LocalDate.now(),
    val departureTime: LocalTime = LocalTime.now(),
    val departureDest: String = "",
    val documentNum: String = "",
    val location: String = "",
    val role: String = "",
    val visitId: String = "",
    val triageAcuity: String = "",
    val dischargeDiagnosis: String = "",
    val dbKey: String = "",
    val triageStatus: StageStatus = StageStatus.NOT_STARTED,
    val informationCollectionStatus: StageStatus = StageStatus.NOT_STARTED,
    val dischargeStatus: StageStatus = StageStatus.NOT_STARTED,
    val complete: Boolean = false
)

enum class StageStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETE
}