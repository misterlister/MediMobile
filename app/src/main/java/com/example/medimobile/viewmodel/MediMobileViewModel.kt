package com.example.medimobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.medimobile.data.eventdata.EventList
import com.example.medimobile.data.eventdata.getDummyEncounters
import com.example.medimobile.data.model.MassGatheringEvent
import com.example.medimobile.data.model.PatientEncounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.LocalTime


class MediMobileViewModel: ViewModel() {

    // **User variables and methods**


    // State to hold the current user
    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser

    // Set the current user
    fun setCurrentUser(user: String) {
        _currentUser.value = user
    }

    // Clear the current user
    fun clearCurrentUser() {
        _currentUser.value = null
    }


    // **Event variables and methods**


    // State to hold the selected event
    private val _selectedEvent = MutableStateFlow<MassGatheringEvent?>(null)
    val selectedEvent: StateFlow<MassGatheringEvent?> = _selectedEvent

    // Set the selected event
    fun setSelectedEvent(eventName: String) {
        _selectedEvent.value = EventList.EVENTS.find { it.eventName == eventName }
    }


    // **Location variables and methods**


    // State to hold the selected event
    private val _selectedLocation = MutableStateFlow<String?>(null)
    val selectedLocation: StateFlow<String?> = _selectedLocation

    // Set the selected event
    fun setSelectedLocation(location: String) {
        _selectedLocation.value = location
    }

    // Initialize the selected event and location if they are null
    init {
        if (_selectedEvent.value == null) {
            _selectedEvent.value = EventList.EVENTS.firstOrNull()
        }
        if (_selectedLocation.value == null) {
            _selectedLocation.value = _selectedEvent.value?.locations?.firstOrNull()?.displayValue
        }
    }


    // **Encounter variables and methods**


    // State to hold the current encounter
    private val _currentEncounter = MutableStateFlow<PatientEncounter?>(null)
    val currentEncounter: StateFlow<PatientEncounter?> get() = _currentEncounter

    // Copy and open an existing encounter
    fun setCurrentEncounter(patientEncounter: PatientEncounter) {
        _currentEncounter.value = patientEncounter.copy()
    }

    // Clear the current encounter
    fun clearCurrentEncounter() {
        _currentEncounter.value = null
    }


    // **Individual setter functions for each field**


    fun setAge(age: Int) {
        _currentEncounter.value = _currentEncounter.value?.copy(age = age)
    }
    fun setArrivalMethod(arrivalMethod: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(arrivalMethod = arrivalMethod)
    }
    fun setArrivalDate(arrivalDate: LocalDate) {
        _currentEncounter.value = _currentEncounter.value?.copy(arrivalDate = arrivalDate)
    }
    fun setArrivalTime(arrivalTime: LocalTime) {
        _currentEncounter.value = _currentEncounter.value?.copy(arrivalTime = arrivalTime)
    }
    fun setChiefComplaint(chiefComplaint: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(chiefComplaint = chiefComplaint)
    }
    fun setComment(comment: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(comment = comment)
    }
    fun setDepartureDate(departureDate: LocalDate) {
        _currentEncounter.value = _currentEncounter.value?.copy(departureDate = departureDate)
    }
    fun setDepartureTime(departureTime: LocalTime) {
        _currentEncounter.value = _currentEncounter.value?.copy(departureTime = departureTime)
    }
    fun setDepartureDest(departureDest: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(departureDest = departureDest)
    }
    fun setDocumentNum(documentNum: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(documentNum = documentNum)
    }
    fun setLocation(location: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(location = location)
    }
    fun setRole(role: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(role = role)
    }
    fun setVisitId(visitId: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(visitId = visitId)
    }
    fun setTriageAcuity(triageAcuity: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(triageAcuity = triageAcuity)
    }
    fun setDischargeDiagnosis(dischargeDiagnosis: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(dischargeDiagnosis = dischargeDiagnosis)
    }
    fun setDbKey(dbKey: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(dbKey = dbKey)
    }


    // **Database functions**

    private var _encounterList = MutableStateFlow<List<PatientEncounter>>(emptyList())
    val encounterList: StateFlow<List<PatientEncounter>> get() = _encounterList

    fun findEncounterByVisitId(visitId: String): PatientEncounter? {
        return _encounterList.value?.find { it.visitId == visitId }
    }

    // Load encounters from database
    fun loadEncountersFromDatabase() {
        _encounterList.value = getDummyEncounters()
        // TODO
    }

    // Save encounter to database
    fun saveEncounterToDatabase() {
        // TODO
    }
}