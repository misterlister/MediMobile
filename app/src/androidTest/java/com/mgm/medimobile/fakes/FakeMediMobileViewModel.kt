package com.mgm.medimobile.fakes

import com.mgm.medimobile.data.model.MassGatheringEvent
import com.mgm.medimobile.data.model.PatientEncounter
import com.mgm.medimobile.testdata.TestData.GROUP_1_EVENT_1
import com.mgm.medimobile.testdata.TestData.GROUP_1_EVENT_2
import com.mgm.medimobile.testdata.TestData.GROUP_2_EVENT_1
import com.mgm.medimobile.testdata.TestData.GROUP_2_EVENT_2
import com.mgm.medimobile.testdata.TestData.INVALID_CREDENTIALS_TOKEN
import com.mgm.medimobile.testdata.TestData.SUCCESS_TOKEN
import com.mgm.medimobile.testdata.TestData.USERNAME_1
import com.mgm.medimobile.testdata.TestData.USERNAME_2
import com.mgm.medimobile.testdata.TestData.VALID_PASSWORD
import com.mgm.medimobile.testdata.TestData.mockEncounters
import com.mgm.medimobile.testdata.TestData.mockEvents
import com.mgm.medimobile.viewmodel.MediMobileViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher

class FakeMediMobileViewModel(
    dispatcher: CoroutineDispatcher = StandardTestDispatcher(),
    loginResultFlow: MutableStateFlow<Result<String>?> = MutableStateFlow(null),
    isLoadingFlow: MutableStateFlow<Boolean> = MutableStateFlow(false),
    userGroupFlow: MutableStateFlow<String?> = MutableStateFlow("TestGroup"),
    selectedEventFlow: MutableStateFlow<MassGatheringEvent?> = MutableStateFlow(null),
    encounterListFlow: MutableStateFlow<List<PatientEncounter>> = MutableStateFlow(emptyList()),
) : MediMobileViewModel() {

    // Backing testable flows
    private val _loginResultFlow = loginResultFlow
    private val _isLoadingFlow = isLoadingFlow
    private val _userGroupFlow = userGroupFlow
    private val _selectedEventFlow = selectedEventFlow
    private val _encounterListFlow = encounterListFlow
    private val _fakeErrorFlow = MutableSharedFlow<String>()

    // Expose read-only versions like real ViewModel
    override val loginResult: StateFlow<Result<String>?> = _loginResultFlow
    override val isLoading: StateFlow<Boolean> = _isLoadingFlow
    override val selectedEvent: StateFlow<MassGatheringEvent?> = _selectedEventFlow
    override val encounterList: StateFlow<List<PatientEncounter>> = _encounterListFlow
    override val errorFlow: SharedFlow<String> = _fakeErrorFlow

    init {
        // Set the selected event to the first mock event from the test data
        setSelectedEvent(mockEvents.first().eventName)
        setCurrentEncounter(PatientEncounter())
    }

    private val scope = CoroutineScope(dispatcher)

    override fun login(email: String, password: String, userGroup: String) {
        scope.launch{
            // Simulate login validation
            if (
                (email == USERNAME_1 || email == USERNAME_2) &&
                (password == VALID_PASSWORD) &&
                    (userGroup == GROUP_1_EVENT_1 ||
                    userGroup == GROUP_2_EVENT_1 ||
                    userGroup == GROUP_2_EVENT_2 ||
                    userGroup == GROUP_1_EVENT_2)
            ) {
                // Success
                emitLoginSuccess()
            } else {
                // Failure
                emitLoginFailure("Invalid credentials or group")
            }
        }
    }

    fun emitLoginSuccess(token: String = SUCCESS_TOKEN) {
        _loginResultFlow.value = Result.success(token)
    }

    fun emitLoginFailure(message: String = INVALID_CREDENTIALS_TOKEN) {
        _loginResultFlow.value = Result.failure(Exception(message))
        scope.launch {
            _fakeErrorFlow.emit(message)  // emit to errorFlow so UI reacts
        }
    }

    override fun setLoading(loading: Boolean) {
        _isLoadingFlow.value = loading
    }

    override fun setSelectedEvent(eventName: String) {
        _selectedEventFlow.value = mockEvents.find { it.eventName == eventName }
    }

    override fun loadEncountersFromDatabase() {
        _encounterListFlow.value = mockEncounters
    }

    var saveSuccess: Boolean = false

    override suspend fun saveEncounterToDatabase(): Boolean {
        // Simulate saving to database
        saveSuccess = true
        return true
    }
}
