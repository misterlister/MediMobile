package com.example.medimobile.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PRIVATE
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medimobile.data.constants.IDConstants
import com.example.medimobile.data.eventdata.EventList
import com.example.medimobile.data.model.DropdownItem
import com.example.medimobile.data.model.MassGatheringEvent
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.model.ServiceLocation
import com.example.medimobile.data.model.StageStatus
import com.example.medimobile.data.remote.AuthApi
import com.example.medimobile.data.remote.ErrorResponse
import com.example.medimobile.data.remote.GetEncountersApi
import com.example.medimobile.data.remote.GetSequenceApi
import com.example.medimobile.data.remote.LoginRequest
import com.example.medimobile.data.remote.SubmitEncountersApi
import com.example.medimobile.data.remote.createRetrofit
import com.example.medimobile.data.utils.DateRangeOption
import com.example.medimobile.data.utils.formatVisitID
import com.example.medimobile.data.utils.isDataEmptyOrNull
import com.example.medimobile.data.utils.mapToPatientEncounterFormData
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    // Currently selected date range for display in the encounter table
    private val _selectedDateRange = MutableStateFlow(DateRangeOption.WEEK)
    val selectedDateRange: StateFlow<DateRangeOption> = _selectedDateRange

    fun setSelectedDateRange(dateRange: DateRangeOption) {
        _selectedDateRange.value = dateRange
    }

    // Boolean to track if low connectivity mode is enabled
    private val _lowConnectivityMode = mutableStateOf(false)
    val lowConnectivityMode: State<Boolean> = _lowConnectivityMode

    fun setLowConnectivityMode(enabled: Boolean) {
        _lowConnectivityMode.value = enabled
        updateRetrofitAndInterfaces() // Update Retrofit with new low connectivity mode setting
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
        updateRetrofitAndInterfaces() // Update Retrofit with new dropdown mappings
    }


    // **Location variables and methods**


    // State to hold the selected event
    private val _selectedLocation = MutableStateFlow<ServiceLocation?>(null)
    val selectedLocation: StateFlow<ServiceLocation?> = _selectedLocation

    // Set the selected event
    fun setSelectedLocation(location: ServiceLocation) {
        _selectedLocation.value = location
    }

    // Initialize the selected event and location if they are null
    init {
        if (_selectedEvent.value == null) {
            _selectedEvent.value = EventList.EVENTS.firstOrNull()
        }
        if (_selectedLocation.value == null) {
            _selectedLocation.value = _selectedEvent.value?.locations?.firstOrNull()
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

    // Initialize a new encounter
    fun initNewEncounter() {
        if (selectedEvent.value == null ||
            selectedLocation.value == null) {
            return
        }
        val newEncounter = PatientEncounter(
            event = _selectedEvent.value!!.eventName,
            location = _selectedLocation.value!!.locationName,
            userUuid = userUuid,
        )
        setCurrentEncounter(newEncounter)
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

    private fun updateEncounterIfChanged(
        transform: (PatientEncounter) -> PatientEncounter
    ) {
        val current = _currentEncounter.value ?: return
        val updated = transform(current)
        if (current != updated) {
            _currentEncounter.value = updated
            markDataChanged()
            updateAllStageStatuses()
        }
    }

    // **Individual setter functions for each field**

    fun setAge(age: Int?) = updateEncounterIfChanged { it.copy(age = age) }

    fun setArrivalMethod(arrivalMethod: String) = updateEncounterIfChanged { it.copy(arrivalMethod = arrivalMethod) }

    fun setArrivalDate(arrivalDate: LocalDate?) = updateEncounterIfChanged { it.copy(arrivalDate = arrivalDate) }

    fun setArrivalTime(arrivalTime: LocalTime?) = updateEncounterIfChanged { it.copy(arrivalTime = arrivalTime) }

    fun setChiefComplaint(chiefComplaint: String) = updateEncounterIfChanged { it.copy(chiefComplaint = chiefComplaint) }

    fun setComment(comment: String) = updateEncounterIfChanged { it.copy(comment = comment) }

    fun setDepartureDate(departureDate: LocalDate?) = updateEncounterIfChanged { it.copy(departureDate = departureDate) }

    fun setDepartureTime(departureTime: LocalTime?) = updateEncounterIfChanged { it.copy(departureTime = departureTime) }

    fun setDepartureDest(departureDest: String) = updateEncounterIfChanged { it.copy(departureDest = departureDest) }

    fun setRole(role: String) = updateEncounterIfChanged { it.copy(role = role) }

    fun setVisitId(visitId: String) = updateEncounterIfChanged { it.copy(visitId = visitId) }

    fun setTriageAcuity(triageAcuity: String) = updateEncounterIfChanged { it.copy(triageAcuity = triageAcuity) }

    fun setDischargeDiagnosis(dischargeDiagnosis: String) = updateEncounterIfChanged { it.copy(dischargeDiagnosis = dischargeDiagnosis) }

    fun markAsSubmitted() {
        _currentEncounter.value = _currentEncounter.value?.copy(submitted = true)
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
    @VisibleForTesting(otherwise = PRIVATE)
    internal fun getDropdownMappings(): Map<String, List<DropdownItem>> {
        val event = _selectedEvent.value ?: return emptyMap()
        return mapOf(
            "arrival_method" to event.dropdowns.arrivalMethods,
            "departure_dest" to event.dropdowns.departureDestinations,
            "role" to event.dropdowns.roles,
            "chief_complaint" to event.dropdowns.chiefComplaints
        )
    }


    // **Retrofit and API interfaces**

    private var retrofit = createRetrofit(_lowConnectivityMode.value, getDropdownMappings())
    private var authApi = retrofit.create(AuthApi::class.java)
    private var getApi = retrofit.create(GetEncountersApi::class.java)
    private var postApi = retrofit.create(SubmitEncountersApi::class.java)
    private var sequenceApi = retrofit.create(GetSequenceApi::class.java)

    // Update the Retrofit instance when the low connectivity mode changes
    private fun updateRetrofitAndInterfaces() {
        retrofit = createRetrofit(_lowConnectivityMode.value, getDropdownMappings())
        authApi = retrofit.create(AuthApi::class.java)
        getApi = retrofit.create(GetEncountersApi::class.java)
        postApi = retrofit.create(SubmitEncountersApi::class.java)
        sequenceApi = retrofit.create(GetSequenceApi::class.java)
    }

    // **Login functions**

    private val _loginResult = MutableStateFlow<Result<String>?>(null)
    val loginResult: StateFlow<Result<String>?> = _loginResult

    private var authToken: String? = null // holds authentication token

    fun login(email: String, password: String, userGroup: String) {
        setLoading(true)
        viewModelScope.launch {
            try {
                val response = authApi.login(LoginRequest(email, password, userGroup))
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        authToken = loginResponse.accessToken
                        _loginResult.value = Result.success(loginResponse.accessToken)
                        Log.d("Login successful", loginResponse.toString())
                    } ?: run {
                        _loginResult.value = Result.failure(Exception("Server returned no data. Please try again later."))
                        Log.d("Login failed", "Server returned no data.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val detailedMessage = parseErrorDetail(errorBody)

                    val errorMessage = when (response.code()) {
                        400 -> detailedMessage ?: "Invalid credentials. Please check your selected Service/Location."
                        401 -> detailedMessage ?: "Invalid credentials. Please check your username and password."
                        403 -> detailedMessage ?: "Access denied. Please check your permissions."
                        422 -> detailedMessage ?: "Invalid email format."
                        500 -> detailedMessage ?: "Server error. Please try again later."
                        else -> detailedMessage ?: "Login failed (code ${response.code()})."
                    }
                    Log.e("Login failed", errorBody.toString())

                    _loginResult.value = Result.failure(Exception(errorMessage))
                }
            } catch (e: IOException) {
                _loginResult.value = Result.failure(Exception("Unable to connect. Please check your internet connection and try again."))
            } catch (e: Exception) {
                _loginResult.value = Result.failure(Exception("Unexpected error occurred. Please try again later."))
            } finally {
                setLoading(false)
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
        // Clear the list
        _encounterList.value = emptyList()
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
                    }
                } else {
                    // Handle error response
                    val errorBody = response.errorBody()?.string()
                    _errorFlow.emit("Failed to fetch encounters. Server responded with an error.")
                    Log.e("DatabaseDebug", "Error: $errorBody")
                }
            } catch (e: IOException) {
                _errorFlow.emit("Network issue detected. Please check your internet connection and try again.")
                Log.e("DatabaseDebug", "Network error: ${e.localizedMessage}")
            } catch (e: Exception) {
                _errorFlow.emit("Unexpected error occurred. Please try again later.")
                Log.e("DatabaseDebug", "Unexpected error: ${e.localizedMessage}")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun parseErrorDetail(jsonString: String?): String? {
        return try {
            if (!jsonString.isNullOrBlank()) {
                val errorResponse = Gson().fromJson(jsonString, ErrorResponse::class.java)
                errorResponse.detail
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun submitNewEncounter(): Boolean {
        setLoading(true)
        return try {
            val formData = mapToPatientEncounterFormData(
                _currentEncounter.value!!,
                getDropdownMappings()
            )
            val response = postApi.postPatientEncounter(formData, getAuthToken())

            if (response.isSuccessful) {
                response.body()?.let { createdEncounter ->
                    Log.d("DatabaseDebug", "Encounter created: $createdEncounter")
                    setCurrentEncounter(createdEncounter)
                    updateEncounterListEntry(createdEncounter)
                } ?: run {
                    _errorFlow.emit("Encounter creation succeeded, but data couldn't be verified. Please try again.")
                    Log.d("DatabaseDebug", "Encounter creation succeeded but response body is null.")
                }
                markAsSubmitted()
                return true
            } else {
                val errorBody = response.errorBody()?.string()
                _errorFlow.emit("Error creating encounter. Please try again later.")
                Log.e("DatabaseDebug", "Error creating encounter: $errorBody")
                return false
            }
        } catch (e: IOException) {
            _errorFlow.emit("Network issue detected. Please check your internet connection and try again.")
            Log.e("DatabaseDebug", "Network error: ${e.localizedMessage}")
            false
        } catch (e: Exception) {
            _errorFlow.emit("Unexpected error occurred. Please try again later.")
            Log.e("DatabaseDebug", "Unexpected error: ${e.localizedMessage}")
            false
        } finally {
            setLoading(false)
        }
    }

    private suspend fun updateExistingEncounter(): Boolean {
        setLoading(true)
        return try {
            val formData = mapToPatientEncounterFormData(
                _currentEncounter.value!!,
                getDropdownMappings()
            )
            val response = postApi.updatePatientEncounter(formData, getAuthToken())

            if (response.isSuccessful) {
                response.body()?.let { updatedEncounter ->
                    Log.d("DatabaseDebug", "Encounter updated: $updatedEncounter")
                    setCurrentEncounter(updatedEncounter)
                    updateEncounterListEntry(updatedEncounter)
                } ?: run {
                    _errorFlow.emit("Encounter update succeeded, but data couldn't be verified. Please try again.")
                    Log.d("DatabaseDebug", "Encounter update succeeded but response body is null.")
                }
                markAsSubmitted()
                return true
            } else {
                val errorBody = response.errorBody()?.string()
                if (response.code() == 403) {
                    _errorFlow.emit("You do not have permission to update this encounter. Please contact an administrator.")
                } else {
                    _errorFlow.emit("Error updating encounter. Please try again later.")
                }
                Log.e("DatabaseDebug", "Error updating encounter: $errorBody")
                return false
            }
        } catch (e: IOException) {
            _errorFlow.emit("Network issue detected. Please check your internet connection and try again.")
            Log.e("DatabaseDebug", "Network error: ${e.localizedMessage}")
            false
        } catch (e: Exception) {
            _errorFlow.emit("Unexpected error occurred. Please try again later.")
            Log.e("DatabaseDebug", "Unexpected error: ${e.localizedMessage}")
            false
        } finally {
            setLoading(false)
        }
    }


    // Save encounter to database
    suspend fun saveEncounterToDatabase(): Boolean {
        val encounter = _currentEncounter.value ?: return false

        _currentEncounter.value = encounter.copy(
            complete = encounter.triageStatus == StageStatus.COMPLETE &&
                    encounter.dischargeStatus == StageStatus.COMPLETE &&
                    encounter.informationCollectionStatus == StageStatus.COMPLETE
        )

        if (_currentEncounter.value!!.encounterUuid == null) {
            // Create new encounter
            return submitNewEncounter()
        } else {
            // Update existing encounter
            return updateExistingEncounter()
        }
    }


    fun generateVisitID() {
        if (_currentEncounter.value == null) {
            viewModelScope.launch {
                _errorFlow.emit("Encounter not created properly, please close and try again.")
            }
            return
        }
        if (_selectedEvent.value == null) {
            viewModelScope.launch {
                _errorFlow.emit("No Event is selected, please select an event first.")
            }
            return
        }
        setLoading(true)

        viewModelScope.launch {
            try {
                val response = sequenceApi.getNextVisitIDSequence()

                if (response.isSuccessful) {
                    response.body()?.let { sequence ->
                        val nextNumber = sequence.nextNumber

                        val visitID = formatVisitID(
                            category = IDConstants.VisitIDCategory.GENERATED,
                            eventID = _selectedEvent.value!!.eventID,
                            locationID = _selectedLocation.value!!.locationID,
                            date = _currentEncounter.value!!.arrivalDate ?: LocalDate.now(),
                            visitSuffix = nextNumber
                        )

                        setVisitId(visitID)
                    } ?: run {
                        _errorFlow.emit("Couldn't generate visit ID. Please try again later.")
                        Log.e("VisitID", "Response body is null")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorFlow.emit("Couldn't generate visit ID. Please try again later.")
                    Log.e("VisitID", "Error response: $errorBody")
                }
            } catch (e: IOException) {
                _errorFlow.emit("Network issue detected. Please check your internet connection and try again.")
                Log.e("VisitID", "Network error: ${e.localizedMessage}")
            } catch (e: Exception) {
                _errorFlow.emit("Unexpected error occurred. Please try again later.")
                Log.e("VisitID", "Unexpected error: ${e.localizedMessage}")
            } finally {
                setLoading(false)
            }
        }
    }
}