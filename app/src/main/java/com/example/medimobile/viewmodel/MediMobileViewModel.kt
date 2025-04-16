package com.example.medimobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medimobile.data.eventdata.EventList
import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.MassGatheringEvent
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.model.StageStatus
import com.example.medimobile.data.model.mapToPatientEncounterFormData
import com.example.medimobile.data.remote.ApiConstants
import com.example.medimobile.data.remote.AuthApi
import com.example.medimobile.data.remote.GetEncountersApi
import com.example.medimobile.data.remote.LocalDateDeserializer
import com.example.medimobile.data.remote.LocalTimeDeserializer
import com.example.medimobile.data.remote.LoginRequest
import com.example.medimobile.data.remote.PatientEncounterDeserializer
import com.example.medimobile.data.remote.SubmitEncountersApi
import com.example.medimobile.data.utils.DateRangeOption
import com.example.medimobile.data.utils.isDataEmptyOrNull
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime


class MediMobileViewModel: ViewModel() {

    // Keeps track of loading state (blocks user interaction when true)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    // Currently selected date range for display in the encounter table
    private val _selectedDateRange = MutableStateFlow(DateRangeOption.WEEK)
    val selectedDateRange: StateFlow<DateRangeOption> = _selectedDateRange

    fun setSelectedDateRange(dateRange: DateRangeOption) {
        _selectedDateRange.value = dateRange
    }

    private fun getMinDate(): String? {
        val currentDate = LocalDate.now()
        val minDate: String? = when (_selectedDateRange.value) {
            DateRangeOption.DAY -> currentDate.minusDays(1).toString()
            DateRangeOption.WEEK -> currentDate.minusWeeks(1).toString()
            DateRangeOption.MONTH -> currentDate.minusMonths(1).toString()
            DateRangeOption.YEAR -> currentDate.minusYears(1).toString()
            DateRangeOption.ALL_TIME -> null
        }
        return minDate
    }

    // **User variables and methods**


    // States to hold the current user and user group
    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser
    private var userUuid: String? = null
    private val _userGroup = MutableStateFlow<String?>(null)
    val userGroup: StateFlow<String?> = _userGroup

    // Set the current user
    fun setCurrentUser(user: String) {
        _currentUser.value = user
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
        updateAllStageStatuses() // Make sure all stage statuses are up to date
        resetDataChanged() // Reset data changed flag
    }

    // Clear the current encounter
    fun clearCurrentEncounter() {
        _currentEncounter.value = null
        resetDataChanged() // Reset data changed flag
    }

    // Keeps track of whether data in the current encounter has been edited
    private val _dataChanged = MutableStateFlow(false)
    val dataChanged: StateFlow<Boolean> = _dataChanged

    fun markDataChanged() {
        _dataChanged.value = true
    }

    private fun resetDataChanged() {
        _dataChanged.value = false
    }

    // **Individual setter functions for each field**


    fun setAge(age: Int) {
        _currentEncounter.value = _currentEncounter.value?.copy(age = age)
    }
    fun setArrivalMethod(arrivalMethod: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(arrivalMethod = arrivalMethod)
    }
    fun setArrivalDate(arrivalDate: LocalDate?) {
        _currentEncounter.value = _currentEncounter.value?.copy(arrivalDate = arrivalDate)
    }
    fun setArrivalTime(arrivalTime: LocalTime?) {
        _currentEncounter.value = _currentEncounter.value?.copy(arrivalTime = arrivalTime)
    }
    fun setChiefComplaint(chiefComplaint: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(chiefComplaint = chiefComplaint)
    }
    fun setComment(comment: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(comment = comment)
    }
    fun setDepartureDate(departureDate: LocalDate?) {
        _currentEncounter.value = _currentEncounter.value?.copy(departureDate = departureDate)
    }
    fun setDepartureTime(departureTime: LocalTime?) {
        _currentEncounter.value = _currentEncounter.value?.copy(departureTime = departureTime)
    }
    fun setDepartureDest(departureDest: String) {
        _currentEncounter.value = _currentEncounter.value?.copy(departureDest = departureDest)
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

    // Update the entry status of the Triage stage
    fun updateTriageStatus() {
        val encounter = currentEncounter.value
        if (encounter != null) {
            // List of fields to check
            val fieldsToCheck = listOf(
                encounter.arrivalDate,
                encounter.arrivalTime,
                encounter.triageAcuity,
                encounter.visitId,
                encounter.arrivalMethod
            )

            // Check if all fields are filled
            val allFieldsFilled = fieldsToCheck.all { !isDataEmptyOrNull(it) }
            // Check if at least one field is filled
            val atLeastOneFieldFilled = fieldsToCheck.any { !isDataEmptyOrNull(it) }

            // Update triage status based on field checks
            val triageStatus = when {
                allFieldsFilled -> StageStatus.COMPLETE
                atLeastOneFieldFilled -> StageStatus.IN_PROGRESS
                else -> StageStatus.NOT_STARTED
            }

            // Apply the new triage status
            _currentEncounter.value = encounter.copy(triageStatus = triageStatus)
        }
    }

    // Update the entry status of the Information Collection stage
    fun updateInformationCollectionStatus() {
        val encounter = currentEncounter.value
        if (encounter != null) {
            // List of fields to check for information collection
            val fieldsToCheck = listOf(
                encounter.age,
                encounter.role,
                encounter.chiefComplaint
            )

            // Check if all fields are filled
            val allFieldsFilled = fieldsToCheck.all { !isDataEmptyOrNull(it) }
            // Check if at least one field is filled
            val atLeastOneFieldFilled = fieldsToCheck.any { !isDataEmptyOrNull(it) }

            // Update information collection status based on field checks
            val informationCollectionStatus = when {
                allFieldsFilled -> StageStatus.COMPLETE
                atLeastOneFieldFilled -> StageStatus.IN_PROGRESS
                else -> StageStatus.NOT_STARTED
            }

            // Apply the new information collection status
            _currentEncounter.value = encounter.copy(informationCollectionStatus = informationCollectionStatus)
        }
    }

    // Update the entry status of the Discharge stage
    fun updateDischargeStatus() {
        val encounter = currentEncounter.value
        if (encounter != null) {
            // List of fields to check for discharge
            val fieldsToCheck = listOf(
                encounter.departureDate,
                encounter.departureTime,
                encounter.departureDest
            )

            // Check if all fields are filled
            val allFieldsFilled = fieldsToCheck.all { !isDataEmptyOrNull(it) }
            // Check if at least one field is filled
            val atLeastOneFieldFilled = fieldsToCheck.any { !isDataEmptyOrNull(it) }

            // Update discharge status based on field checks
            val dischargeStatus = when {
                allFieldsFilled -> StageStatus.COMPLETE
                atLeastOneFieldFilled -> StageStatus.IN_PROGRESS
                else -> StageStatus.NOT_STARTED
            }

            // Apply the new discharge status
            _currentEncounter.value = encounter.copy(dischargeStatus = dischargeStatus)
        }
    }

    // Update all stage statuses
    private fun updateAllStageStatuses() {
        updateTriageStatus()
        updateInformationCollectionStatus()
        updateDischargeStatus()
    }

    // **API functions**

    private fun getDropdownMappings(): Map<String, List<DropdownItem>> {
        val event = _selectedEvent.value ?: return emptyMap()
        return mapOf(
            "arrival_method" to event.dropdowns.arrivalMethods,
            "departure_dest" to event.dropdowns.departureDestinations,
            "role" to event.dropdowns.roles,
            "chief_complaint" to event.dropdowns.chiefComplaints
        )
    }

    // custom deserializers to fix date/time parsing
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
        .registerTypeAdapter(PatientEncounter::class.java, PatientEncounterDeserializer(getDropdownMappings()))
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    // **Login functions**


    private val authApi = retrofit.create(AuthApi::class.java)

    private val _loginResult = MutableStateFlow<Result<String>?>(null)
    val loginResult: StateFlow<Result<String>?> = _loginResult

    private var authToken: String? = null // holds authentication token

    fun login(email: String, password: String, userGroup: String) {
        viewModelScope.launch {
            try {
                val response = authApi.login(LoginRequest(email, password, userGroup))
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        Log.d("LoginDebug", "Login successful: $loginResponse")
                        authToken = loginResponse.accessToken
                        _loginResult.value = Result.success(loginResponse.accessToken)
                    } ?: run {
                        _loginResult.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _loginResult.value = Result.failure(Exception("Login failed: $errorBody"))
                }
            } catch (e: IOException) {
                _loginResult.value = Result.failure(Exception("Network error: ${e.localizedMessage}"))
            } catch (e: Exception) {
                _loginResult.value = Result.failure(Exception("Unexpected error: ${e.localizedMessage}"))
            }
        }
    }

    private fun getAuthToken(): String {
        if (authToken == null) {
            return ""
        }
        return "Bearer $authToken"
    }

    fun logout() {
        authToken = null  // Clear token when logging out
        _loginResult.value = null
        _userGroup.value = null
        _currentUser.value = null
    }

    // **Database functions**

    private val getApi = retrofit.create(GetEncountersApi::class.java)

    private var _encounterList = MutableStateFlow<List<PatientEncounter>>(emptyList())
    val encounterList: StateFlow<List<PatientEncounter>> get() = _encounterList

    fun findEncounterByVisitId(visitId: String): PatientEncounter? {
        return _encounterList.value.find { it.visitId == visitId }
    }

    private fun updateEncounterListEntry(updatedEncounter: PatientEncounter) {
        // Check if the encounter already exists by encounterUuid
        val existingEncounter = _encounterList.value.find { it.encounterUuid == updatedEncounter.encounterUuid }

        if (existingEncounter != null) {
            // Encounter exists, replace the old encounter with the updated one
            _encounterList.value = _encounterList.value.map {
                if (it.encounterUuid == updatedEncounter.encounterUuid) {
                    updatedEncounter // Replace with the updated encounter
                } else {
                    it // Keep the other encounters unchanged
                }
            }
        } else {
            // Encounter doesn't exist, add the new encounter to the list
            _encounterList.value += updatedEncounter
        }
    }

    // Load encounters from database
    fun loadEncountersFromDatabase() {
        setLoading(true)
        viewModelScope.launch {
            try {
                val response = getApi.getPatientEncounters(
                    userUuid = null,
                    arrivalDateMin = getMinDate(),
                    arrivalDateMax = null,
                    getAuthToken())

                if (response.isSuccessful) {
                    response.body()?.let { encounters ->

                        Log.d("DatabaseDebug", "Encounters fetched: $encounters")

                        _encounterList.value = encounters
                    } ?: run {

                        Log.d("DatabaseDebug", "No encounters found.")

                        _encounterList.value = emptyList()
                    }
                } else {
                    // Handle error response
                    val errorBody = response.errorBody()?.string()
                    Log.e("DatabaseDebug", "Error: $errorBody")
                }
            } catch (e: IOException) {
                Log.e("DatabaseDebug", "Network error: ${e.localizedMessage}")
            } catch (e: Exception) {
                // Log unexpected errors
                Log.e("DatabaseDebug", "Unexpected error: ${e.localizedMessage}")
            } finally {
                setLoading(false)
            }
        }
    }

    private val postApi = retrofit.create(SubmitEncountersApi::class.java)

    private fun submitNewEncounter() {
        setLoading(true)
        viewModelScope.launch {
            try {
                val formData = mapToPatientEncounterFormData(_currentEncounter.value!!)
                val response = postApi.postPatientEncounter(formData, getAuthToken())

                if (response.isSuccessful) {
                    // Handle successful response
                    response.body()?.let { createdEncounter ->
                        Log.d("DatabaseDebug", "Encounter created: $createdEncounter")
                        // Update the encounter with the new encounterUuid
                        _currentEncounter.value = createdEncounter
                        updateEncounterListEntry(createdEncounter)
                    } ?: run {
                        Log.d("DatabaseDebug", "Encounter creation succeeded but response body is null.")
                    }
                } else {
                    // Handle error response
                    val errorBody = response.errorBody()?.string()
                    Log.e("DatabaseDebug", "Error creating encounter: $errorBody")
                }
            } catch (e: IOException) {
                Log.e("DatabaseDebug", "Network error: ${e.localizedMessage}")
            } catch (e: Exception) {
                Log.e("DatabaseDebug", "Unexpected error: ${e.localizedMessage}")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun updateExistingEncounter() {
        setLoading(true)
        viewModelScope.launch {
            try {
                val formData = mapToPatientEncounterFormData(_currentEncounter.value!!)
                val response = postApi.updatePatientEncounter(formData, getAuthToken())

                if (response.isSuccessful) {
                    // Handle successful response
                    response.body()?.let { createdEncounter ->
                        Log.d("DatabaseDebug", "Encounter updated: $createdEncounter")
                        // Update the encounter with the new encounterUuid
                        _currentEncounter.value = createdEncounter
                        updateEncounterListEntry(createdEncounter)
                    } ?: run {
                        Log.d("DatabaseDebug", "Encounter update succeeded but response body is null.")
                    }
                } else {
                    // Handle error response
                    val errorBody = response.errorBody()?.string()
                    Log.e("DatabaseDebug", "Error updating encounter: $errorBody")
                }
            } catch (e: IOException) {
                Log.e("DatabaseDebug", "Network error: ${e.localizedMessage}")
            } catch (e: Exception) {
                Log.e("DatabaseDebug", "Unexpected error: ${e.localizedMessage}")
            } finally {
                setLoading(false)
            }
        }
    }



    // Save encounter to database
    fun saveEncounterToDatabase() {
        if (_currentEncounter.value == null) {
            return
        }

        if(_currentEncounter.value!!.triageStatus == StageStatus.COMPLETE &&
            _currentEncounter.value!!.dischargeStatus == StageStatus.COMPLETE &&
            _currentEncounter.value!!.informationCollectionStatus == StageStatus.COMPLETE) {
            // If all stages are complete, set the encounter as complete
            _currentEncounter.value = currentEncounter.value!!.copy(complete = true)
        } else {
            // If any stage is not complete, set the encounter as incomplete
            _currentEncounter.value = currentEncounter.value!!.copy(complete = false)
        }

        if (_currentEncounter.value!!.encounterUuid == null) {
            // Create a new encounter in Database
            submitNewEncounter()
        } else {
            // Update an existing encounter in Database
            updateExistingEncounter()
        }
    }
}