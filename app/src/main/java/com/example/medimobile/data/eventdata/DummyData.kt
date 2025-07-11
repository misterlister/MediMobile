package com.example.medimobile.data.eventdata

import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.model.StageStatus
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun getDummyEncounters(): List<PatientEncounter> {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    return listOf(
        PatientEncounter(
            arrivalDate = LocalDate.parse("2025-02-19", dateFormatter),
            arrivalTime = LocalTime.parse("2025-02-19 08:00", timeFormatter),
            arrivalMethod = "Ambulance",
            age = 30,
            chiefComplaint = "Headache",
            comment = "No allergies",
            departureDate = LocalDate.parse("2025-02-19", dateFormatter),
            departureTime = LocalTime.parse("2025-02-19 14:30", timeFormatter),
            departureDest = "Home",
            location = "ER",
            role = "Patient",
            visitId = "GSHAM0125-00001",
            triageAcuity = "Green",
            dischargeDiagnosis = "Migraine",
            encounterUuid = "E001",
            arrivalStatus = StageStatus.IN_PROGRESS,
            triageStatus = StageStatus.NOT_STARTED,
            dischargeStatus = StageStatus.COMPLETE,
            complete = false
        ),
        PatientEncounter(
            arrivalDate = LocalDate.parse("2025-02-18", dateFormatter),
            arrivalTime = LocalTime.parse("2025-02-18 10:00", timeFormatter),
            arrivalMethod = "Walk-in",
            age = 45,
            chiefComplaint = "Chest Pain",
            comment = "Family history of heart disease",
            departureDate = LocalDate.parse("2025-02-18", dateFormatter),
            departureTime = LocalTime.parse("2025-02-18 16:00", timeFormatter),
            departureDest = "ICU",
            location = "ER",
            role = "Patient",
            visitId = "QSHAM0125-00201",
            triageAcuity = "Yellow",
            dischargeDiagnosis = "Angina",
            encounterUuid = "E002",
            arrivalStatus = StageStatus.COMPLETE,
            triageStatus = StageStatus.IN_PROGRESS,
            dischargeStatus = StageStatus.NOT_STARTED,
            complete = true
        ),
        PatientEncounter(
            arrivalDate = LocalDate.parse("2025-02-17", dateFormatter),
            arrivalTime = LocalTime.parse("2025-02-17 07:00", timeFormatter),
            arrivalMethod = "Helicopter",
            age = 60,
            chiefComplaint = "Stroke symptoms",
            comment = "High blood pressure",
            departureDate = LocalDate.parse("2025-02-17", dateFormatter),
            departureTime = LocalTime.parse("2025-02-17 20:00", timeFormatter),
            departureDest = "Neurology",
            location = "ER",
            role = "Patient",
            visitId = "QSHAM0125-00043",
            triageAcuity = "Red",
            dischargeDiagnosis = "Stroke",
            encounterUuid = "E003",
            arrivalStatus = StageStatus.NOT_STARTED,
            triageStatus = StageStatus.COMPLETE,
            dischargeStatus = StageStatus.IN_PROGRESS,
            complete = false
        )
    )
}
