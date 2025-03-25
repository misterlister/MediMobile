package com.example.medimobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medimobile.data.eventdata.EventList
import com.example.medimobile.data.eventdata.getDummyEncounters
import com.example.medimobile.data.model.MassGatheringEvent
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.remote.ApiConstants
import com.example.medimobile.data.remote.AuthApi
import com.example.medimobile.data.remote.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime


class MediMobileViewModel: ViewModel() {

    // **User variables and methods**


    // States to hold the current user and user group
    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser
    private val _userGroup = MutableStateFlow<String?>(null)
    val userGroup: StateFlow<String?> = _userGroup

    // Set the current user
    fun setCurrentUser(user: String) {
        _currentUser.value = user
    }

    // Clear the current user
    fun clearCurrentUser() {
        _currentUser.value = null
    }

    // Set the user group
    fun setUserGroup(group: String) {
        _userGroup.value = group
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


    // **API functions**


    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    // **Login functions**


    private val authApi = retrofit.create(AuthApi::class.java)

    private val _loginResult = MutableStateFlow<Result<String>?>(null)
    val loginResult: StateFlow<Result<String>?> = _loginResult

    private var authToken: String? = null // holds authentication token

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authApi.login(LoginRequest(username, password))

                authToken = response.token
                _loginResult.value = Result.success(response.token)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun getToken(): String? {
        return authToken  // Retrieve token when needed
    }

    fun logout() {
        authToken = null  // Clear token when logging out
        _loginResult.value = null
    }

    // **Database functions**

    private var _encounterList = MutableStateFlow<List<PatientEncounter>>(emptyList())
    val encounterList: StateFlow<List<PatientEncounter>> get() = _encounterList

    fun findEncounterByVisitId(visitId: String): PatientEncounter? {
        return _encounterList.value.find { it.visitId == visitId }
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