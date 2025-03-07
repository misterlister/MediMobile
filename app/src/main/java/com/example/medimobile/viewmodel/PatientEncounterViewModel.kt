package com.example.medimobile.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.medimobile.data.model.PatientEncounter
import java.time.LocalDate
import java.time.LocalTime


class PatientEncounterViewModel: ViewModel() {
    private val _encounter = mutableStateOf(PatientEncounter())
    val encounter: State<PatientEncounter> get() = _encounter

    // Import Encounter from Database
    fun setEncounterFromDatabase(patientEncounter: PatientEncounter) {
        _encounter.value = patientEncounter
    }
    // Individual setter functions for each field
    fun setAge(age: Int) {
        _encounter.value = _encounter.value.copy(age = age)
    }
    fun setArrivalMethod(arrivalMethod: String) {
        _encounter.value = _encounter.value.copy(arrivalMethod = arrivalMethod)
    }
    fun setArrivalDate(arrivalDate: LocalDate) {
        _encounter.value = _encounter.value.copy(arrivalDate = arrivalDate)
    }
    fun setArrivalTime(arrivalTime: LocalTime) {
        _encounter.value = _encounter.value.copy(arrivalTime = arrivalTime)
    }
    fun setChiefComplaint(chiefComplaint: String) {
        _encounter.value = _encounter.value.copy(chiefComplaint = chiefComplaint)
    }
    fun setComment(comment: String) {
        _encounter.value = _encounter.value.copy(comment = comment)
    }
    fun setDepartureDate(departureDate: LocalDate) {
        _encounter.value = _encounter.value.copy(departureDate = departureDate)
    }
    fun setDepartureTime(departureTime: LocalTime) {
        _encounter.value = _encounter.value.copy(departureTime = departureTime)
    }
    fun setDepartureDest(departureDest: String) {
        _encounter.value = _encounter.value.copy(departureDest = departureDest)
    }
    fun setDocumentNum(documentNum: String) {
        _encounter.value = _encounter.value.copy(documentNum = documentNum)
    }
    fun setLocation(location: String) {
        _encounter.value = _encounter.value.copy(location = location)
    }
    fun setRole(role: String) {
        _encounter.value = _encounter.value.copy(role = role)
    }
    fun setVisitId(visitId: String) {
        _encounter.value = _encounter.value.copy(visitId = visitId)
    }
    fun setTriageAcuity(triageAcuity: String) {
        _encounter.value = _encounter.value.copy(triageAcuity = triageAcuity)
    }
    fun setDischargeDiagnosis(dischargeDiagnosis: String) {
        _encounter.value = _encounter.value.copy(dischargeDiagnosis = dischargeDiagnosis)
    }
    fun setDbKey(dbKey: String) {
        _encounter.value = _encounter.value.copy(dbKey = dbKey)
    }

    // Save encounter to database
    fun saveEncounterToDatabase() {
        // TODO
    }
}