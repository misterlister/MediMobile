package com.example.medimobile.ui.screens.dataentry

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.medimobile.fakes.FakeMediMobileViewModel
import com.example.medimobile.testdata.TestData.FAKE_DIAGNOSIS
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DischargeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeViewModel: FakeMediMobileViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setup() {
        fakeViewModel = FakeMediMobileViewModel(dispatcher = testDispatcher)
        composeTestRule.setContent {
            DischargeScreen(viewModel = fakeViewModel)
        }
    }

    @Test
    fun dischargeScreen_showsFields() {
        composeTestRule.onNodeWithTag("nowDateTimeButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("dateButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hourDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("minuteDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("departureDestinationDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("dischargeDiagnosisTextField").assertIsDisplayed()
    }

    @Test
    fun dischargeScreen_DepartureDestinationDropdown_updatesViewModel() {
        composeTestRule.onNodeWithTag("departureDestinationDropdown").performClick()
        composeTestRule.onNodeWithText("Destination 2").performClick()

        assertEquals("Destination 2", fakeViewModel.currentEncounter.value?.departureDest)
    }

    @Test
    fun dischargeScreen_DischargeDiagnosisTextField_updatesViewModel() {
        composeTestRule.onNodeWithTag("dischargeDiagnosisTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("dischargeDiagnosisTextField")
            .performTextInput(FAKE_DIAGNOSIS)

        assertEquals(FAKE_DIAGNOSIS, fakeViewModel.currentEncounter.value?.dischargeDiagnosis)
    }
}