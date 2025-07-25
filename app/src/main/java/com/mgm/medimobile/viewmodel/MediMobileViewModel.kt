package com.mgm.medimobile.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PRIVATE
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgm.medimobile.data.constants.IDConstants
import com.mgm.medimobile.data.eventdata.EventList
import com.mgm.medimobile.data.model.DropdownItem
import com.mgm.medimobile.data.model.MassGatheringEvent
import com.mgm.medimobile.data.model.PatientEncounter
import com.mgm.medimobile.data.model.ServiceLocation
import com.mgm.medimobile.data.model.StageStatus
import com.mgm.medimobile.data.remote.AuthApi
import com.mgm.medimobile.data.remote.ErrorResponse
import com.mgm.medimobile.data.remote.GetEncountersApi
import com.mgm.medimobile.data.remote.GetSequenceApi
import com.mgm.medimobile.data.remote.LoginRequest
import com.mgm.medimobile.data.remote.SubmitEncountersApi
import com.mgm.medimobile.data.remote.createRetrofit
import com.mgm.medimobile.data.session.UserSessionManager
import com.mgm.medimobile.data.utils.DateRangeOption
import com.mgm.medimobile.data.utils.formatVisitID
import com.mgm.medimobile.data.utils.isDataEmptyOrNull
import com.mgm.medimobile.data.utils.mapToPatientEncounterFormData
import com.mgm.medimobile.data.utils.toLoggedInUser
import com.mgm.medimobile.ui.theme.BrightnessMode
import com.mgm.medimobile.ui.theme.ContrastLevel
import com.google.gson.Gson
import com.mgm.medimobile.data.constants.ApiConstants.TIMEOUT_MESSAGE
import com.mgm.medimobile.data.remote.ApiResponseType
import com.mgm.medimobile.data.remote.GetEncounterByVisitIdApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


open class MediMobileViewModel: ViewModel() {

    // **UI state variables**

    // Brightness Mode (Light mode, Dark mode, or System setting)
    private val _brightnessMode = mutableStateOf(BrightnessMode.SYSTEM)
    val brightnessMode: State<BrightnessMode> = _brightnessMode

    fun setBrightnessMode(setting: BrightnessMode) {
        _brightnessMode.value = setting
    }

    // Contrast level (low, medium, or high)
    private val _contrastLevel = mutableStateOf(ContrastLevel.MEDIUM)
    val contrastLevel: State<ContrastLevel> = _contrastLevel

    fun setContrastLevel(level: ContrastLevel) {
        _contrastLevel.value = level
    }

    // Keeps track of loading state (blocks user interaction when true)
    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    open fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()


    private val _errorFlow = MutableSharedFlow<String>()
    open val errorFlow = _errorFlow.asSharedFlow()

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
        val currentDate = LocalDateTime.now()
        val minDate: String? = when (_selectedDateRange.value) {
            DateRangeOption.DAY -> currentDate.minusDays(1).toString()
            DateRangeOption.WEEK -> currentDate.minusWeeks(1).toString()
            DateRangeOption.MONTH -> currentDate.minusMonths(1).toString()
            DateRangeOption.YEAR -> currentDate.minusYears(1).toString()
            DateRangeOption.ALL_TIME -> null
        }
        return minDate
    }

    // **Event variables and methods**

    // State to hold the selected event
    private val _selectedEvent = MutableStateFlow<MassGatheringEvent?>(null)
    open val selectedEvent: StateFlow<MassGatheringEvent?> = _selectedEvent

    // Set the selected event
    open fun setSelectedEvent(eventName: String) {
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
    fun setCurrentEncounter(patientEncounter: PatientEncounter, lockVisitId: Boolean = false) {
        _currentEncounter.value = patientEncounter.copy()
        updateAllStageStatuses() // Make sure all stage statuses are up to date
        resetDataChanged() // Reset data changed flag
        if (lockVisitId) {
            markAsSubmitted()
        }
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
            location = _selectedLocation.value!!.locationName
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

    // Update the entry status of the Arrival stage
    fun updateArrivalStatus() {
        val encounter = currentEncounter.value
        if (encounter != null) {
            // List of fields to check
            val fieldsToCheck = listOf(
                encounter.arrivalDate,
                encounter.arrivalTime,
                encounter.visitId,
                encounter.arrivalMethod,
                encounter.age,
                encounter.role,
            )

            // Check if all fields are filled
            val allFieldsFilled = fieldsToCheck.all { !isDataEmptyOrNull(it) }
            // Check if at least one field is filled
            val atLeastOneFieldFilled = fieldsToCheck.any { !isDataEmptyOrNull(it) }

            // Update Arrival status based on field checks
            val arrivalStatus = when {
                allFieldsFilled -> StageStatus.COMPLETE
                atLeastOneFieldFilled -> StageStatus.IN_PROGRESS
                else -> StageStatus.NOT_STARTED
            }

            // Apply the new Arrival status
            _currentEncounter.value = encounter.copy(arrivalStatus = arrivalStatus)
        }
    }

    // Update the entry status of the Triage stage
    fun updateTriageStatus() {
        val encounter = currentEncounter.value
        if (encounter != null) {
            // List of fields to check for Triage
            val fieldsToCheck = listOf(
                encounter.triageAcuity,
                encounter.chiefComplaint
            )

            // Check if all fields are filled
            val allFieldsFilled = fieldsToCheck.all { !isDataEmptyOrNull(it) }
            // Check if at least one field is filled
            val atLeastOneFieldFilled = fieldsToCheck.any { !isDataEmptyOrNull(it) }

            // Update Triage status based on field checks
            val triageStatus = when {
                allFieldsFilled -> StageStatus.COMPLETE
                atLeastOneFieldFilled -> StageStatus.IN_PROGRESS
                else -> StageStatus.NOT_STARTED
            }

            // Apply the new Triage status
            _currentEncounter.value = encounter.copy(triageStatus = triageStatus)
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
        updateArrivalStatus()
        updateTriageStatus()
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
    private var getByVisitIdApi = retrofit.create(GetEncounterByVisitIdApi::class.java)

    // Update the Retrofit instance when the low connectivity mode changes
    private fun updateRetrofitAndInterfaces() {
        retrofit = createRetrofit(_lowConnectivityMode.value, getDropdownMappings())
        authApi = retrofit.create(AuthApi::class.java)
        getApi = retrofit.create(GetEncountersApi::class.java)
        postApi = retrofit.create(SubmitEncountersApi::class.java)
        sequenceApi = retrofit.create(GetSequenceApi::class.java)
        getByVisitIdApi = retrofit.create(GetEncounterByVisitIdApi::class.java)
    }

    // **Login functions**

    private val _loginResult = MutableStateFlow<Result<String>?>(null)
    open val loginResult: StateFlow<Result<String>?> = _loginResult

    open fun login(email: String, password: String, userGroup: String) {
        setLoading(true)
        viewModelScope.launch {
            try {
                val response = authApi.login(LoginRequest(email, password, userGroup))
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        val user = loginResponse.toLoggedInUser(email = email, group = userGroup)
                        UserSessionManager.login(user)
                        _loginResult.value = Result.success(loginResponse.accessToken)
                        Log.d("Login successful", loginResponse.toString())
                    } ?: run {
                        _errorFlow.emit("Server returned no data. Please try again later.")
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
                    _errorFlow.emit(errorMessage)
                }
            } catch (e: IOException) {
                _errorFlow.emit("Unable to connect. Please check your internet connection and try again.")
                Log.d("Login failed", "Network error: ${e.localizedMessage}")
            } catch (e: Exception) {
                _errorFlow.emit("Unexpected error occurred. Please try again later.")
                Log.d("Login failed", "Unexpected error: ${e.localizedMessage}")
            } finally {
                setLoading(false)
            }
        }
    }

    fun clearLoginResult() {
        _loginResult.value = null
    }

    fun logout() {
        UserSessionManager.logout()
        clearLoginResult()
        viewModelScope.launch {
            _logoutEvent.emit(Unit)
        }
    }

    // **Database functions**

    private var _encounterList = MutableStateFlow<List<PatientEncounter>>(emptyList())
    open val encounterList: StateFlow<List<PatientEncounter>> get() = _encounterList

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
    open fun loadEncountersFromDatabase() {
        // Clear the list
        _encounterList.value = emptyList()
        setLoading(true)
        viewModelScope.launch {
            try {
                val response = getApi.getPatientEncounters(
                    userUuid = null,
                    arrivalDateTimeMin = getMinDate(),
                    arrivalDateTimeMax = null,
                    token = UserSessionManager.getAuthToken()
                )

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

                    val errorMessage = when (response.code()) {
                        422 -> TIMEOUT_MESSAGE
                        else -> "Failed to fetch encounters. Server responded with an error."
                    }

                    _errorFlow.emit(errorMessage)
                    Log.e("DatabaseDebug", "Error: $errorBody")

                    if (response.code() == 422) {
                        logout()
                    }
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


    // Return an encounter with a specific visit ID if it exists in the database
    open suspend fun getEncounterByVisitId(visitID: String): PatientEncounter? {
        var foundEncounter: PatientEncounter? = null
        setLoading(true)
        try {
            val response = getByVisitIdApi.getEncounterByVisitId(
                visitId = visitID,
                token = UserSessionManager.getAuthToken()
            )

            if (response.isSuccessful) {
                response.body()?.let { encounter ->

                    Log.d("DatabaseDebug", "Encounter fetched: $encounter")

                    foundEncounter = encounter
                } ?: run {
                    Log.d("DatabaseDebug", "No matching encounter found.")
                }
            } else {
                // Handle error response
                val errorBody = response.errorBody()?.string()

                if (response.code() == 404) {
                    Log.d("DatabaseDebug", "No matching encounter found.")
                    return null
                }

                val errorMessage = when (response.code()) {
                    422 -> TIMEOUT_MESSAGE
                    else -> "Failed to fetch encounter. Server responded with an error."
                }

                _errorFlow.emit(errorMessage)
                Log.e("DatabaseDebug", "Error: $errorBody")

                if (response.code() == 422) {
                    logout()
                }
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
        return foundEncounter
    }


    private fun parseErrorDetail(jsonString: String?): String? {
        return try {
            if (!jsonString.isNullOrBlank() && jsonString.trimStart().startsWith("{")) {
                val errorResponse = Gson().fromJson(jsonString, ErrorResponse::class.java)
                errorResponse.detail
            } else {
                return null
            }
        } catch (e: Exception) {
            Log.e("parseErrorDetail", "Failed to parse error JSON: $jsonString", e)
            return null
        }
    }

    private suspend fun submitNewEncounter(): ApiResponseType {
        setLoading(true)
        return try {
            val formData = mapToPatientEncounterFormData(
                _currentEncounter.value!!,
                getDropdownMappings()
            )
            val response = postApi.postPatientEncounter(formData, UserSessionManager.getAuthToken())

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
                ApiResponseType.SUCCESS
            } else {
                val errorBody = response.errorBody()?.string()
                val detailedMessage = parseErrorDetail(errorBody)
                val errorMessage: String
                val responseCode: ApiResponseType

                when (response.code()) {
                    422 -> {
                        errorMessage = TIMEOUT_MESSAGE
                        responseCode = ApiResponseType.LOGOUT
                    }
                    else -> {
                        errorMessage = detailedMessage ?: "Error creating encounter: (code ${response.code()})."
                        responseCode = ApiResponseType.FAILURE
                    }
                }

                _errorFlow.emit(errorMessage)
                Log.e("DatabaseDebug", "Error creating encounter: $errorBody")

                responseCode // Return the response code to the caller
            }
        } catch (e: IOException) {
            _errorFlow.emit("Network issue detected. Please check your internet connection and try again.")
            Log.e("DatabaseDebug", "Network error: ${e.localizedMessage}")
            ApiResponseType.FAILURE
        } catch (e: Exception) {
            _errorFlow.emit("Unexpected error occurred. Please try again later.")
            Log.e("DatabaseDebug", "Unexpected error: ${e.localizedMessage}")
            ApiResponseType.FAILURE
        } finally {
            setLoading(false)
        }
    }

    private suspend fun updateExistingEncounter(): ApiResponseType {
        setLoading(true)
        return try {
            val formData = mapToPatientEncounterFormData(
                _currentEncounter.value!!,
                getDropdownMappings()
            )
            val response = postApi.updatePatientEncounter(formData, UserSessionManager.getAuthToken())

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
                ApiResponseType.SUCCESS
            } else {
                val errorBody = response.errorBody()?.string()
                val detailedMessage = parseErrorDetail(errorBody)
                val errorMessage: String
                val responseCode: ApiResponseType

                when (response.code()) {
                    403 -> {
                        errorMessage = "You do not have permission to update this encounter. Please contact an administrator."
                        responseCode = ApiResponseType.FAILURE
                    }
                    422 -> {
                        errorMessage = TIMEOUT_MESSAGE
                        responseCode = ApiResponseType.LOGOUT
                    }
                    else -> {
                        errorMessage = detailedMessage ?: "Error updating encounter: (code ${response.code()})."
                        responseCode = ApiResponseType.FAILURE
                    }
                }

                Log.e("DatabaseDebug", "Error updating encounter: $errorBody")

                _errorFlow.emit(errorMessage)

                responseCode // Return the response code to the caller
            }
        } catch (e: IOException) {
            _errorFlow.emit("Network issue detected. Please check your internet connection and try again.")
            Log.e("DatabaseDebug", "Network error: ${e.localizedMessage}")
            ApiResponseType.FAILURE
        } catch (e: Exception) {
            _errorFlow.emit("Unexpected error occurred. Please try again later.")
            Log.e("DatabaseDebug", "Unexpected error: ${e.localizedMessage}")
            ApiResponseType.FAILURE
        } finally {
            setLoading(false)
        }
    }


    // Save encounter to database
    open suspend fun saveEncounterToDatabase(): ApiResponseType {
        val encounter = _currentEncounter.value ?: return ApiResponseType.FAILURE

        _currentEncounter.value = encounter.copy(
            complete = encounter.arrivalStatus == StageStatus.COMPLETE &&
                    encounter.dischargeStatus == StageStatus.COMPLETE &&
                    encounter.triageStatus == StageStatus.COMPLETE
        )

        if (_currentEncounter.value!!.encounterUuid == null) {
            // Check if this VisitID is a duplicate of an existing one
            if (encounterList.value.any { it.visitId == encounter.visitId }) {
                _errorFlow.emit("This Visit ID is already assigned to an existing Encounter. Please use another.")
                return ApiResponseType.FAILURE
            }
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