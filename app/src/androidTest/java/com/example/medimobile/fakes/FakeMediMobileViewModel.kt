package com.example.medimobile.fakes

import com.example.medimobile.data.model.MassGatheringEvent
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.testdata.TestData.INVALID_CREDENTIALS_TOKEN
import com.example.medimobile.testdata.TestData.SUCCESS_TOKEN
import com.example.medimobile.testdata.TestData.GROUP_1_EVENT_1
import com.example.medimobile.testdata.TestData.GROUP_1_EVENT_2
import com.example.medimobile.testdata.TestData.GROUP_2_EVENT_1
import com.example.medimobile.testdata.TestData.GROUP_2_EVENT_2
import com.example.medimobile.testdata.TestData.VALID_PASSWORD
import com.example.medimobile.testdata.TestData.USERNAME_1
import com.example.medimobile.testdata.TestData.USERNAME_2
import com.example.medimobile.testdata.TestData.mockEncounters
import com.example.medimobile.testdata.TestData.mockEvents
import com.example.medimobile.viewmodel.MediMobileViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
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

    // Expose read-only versions like real ViewModel
    override val loginResult: StateFlow<Result<String>?> = _loginResultFlow
    override val isLoading: StateFlow<Boolean> = _isLoadingFlow
    override val userGroup: StateFlow<String?> = _userGroupFlow
    override val selectedEvent: StateFlow<MassGatheringEvent?> = _selectedEventFlow
    override val encounterList: StateFlow<List<PatientEncounter>> = _encounterListFlow

    init {
        // Set the selected event to the first mock event from the test data
        setSelectedEvent(mockEvents.first().eventName)
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
    }

    override fun setLoading(loading: Boolean) {
        _isLoadingFlow.value = loading
    }

    override fun setUserGroup(group: String) {
        _userGroupFlow.value = group
    }

    override fun setSelectedEvent(eventName: String) {
        _selectedEventFlow.value = mockEvents.find { it.eventName == eventName }
    }

    override fun loadEncountersFromDatabase() {
        _encounterListFlow.value = mockEncounters
    }
}
