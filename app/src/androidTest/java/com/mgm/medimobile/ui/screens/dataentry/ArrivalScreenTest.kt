package com.mgm.medimobile.ui.screens.dataentry

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.mgm.medimobile.fakes.FakeMediMobileViewModel
import com.mgm.medimobile.testdata.TestData.FAKE_VISIT_ID
import com.mgm.medimobile.testdata.TestData.mockEncounters
import com.mgm.medimobile.ui.theme.MediMobileTheme
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArrivalScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeViewModel: FakeMediMobileViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setup() {
        fakeViewModel = FakeMediMobileViewModel(dispatcher = testDispatcher)
        composeTestRule.setContent {
            MediMobileTheme(
                brightnessMode = fakeViewModel.brightnessMode.value,
                contrastLevel = fakeViewModel.contrastLevel.value
            ) {
                ArrivalScreen(viewModel = fakeViewModel)
            }
        }
    }

    @Test
    fun arrivalScreen_showsFields() {
        composeTestRule.onNodeWithTag("nowDateTimeButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("dateButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hourDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("minuteDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("visitIdTextField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("visitIdScannerButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("generateVisitIdButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("arrivalMethodDropdown").assertIsDisplayed()
    }

    @Test
    fun arrivalScreen_disablesVisitIdControls_whenSubmitted() {
        fakeViewModel.setCurrentEncounter(mockEncounters[1])
        fakeViewModel.markAsSubmitted()

        composeTestRule.onNodeWithTag("visitIdTextField").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("generateVisitIdButton").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("visitIdScannerButton").assertIsNotEnabled()
    }

    @Test
    fun arrivalScreen_visitIdTextField_updatesViewModel() {
        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextInput(FAKE_VISIT_ID)

        assertEquals(FAKE_VISIT_ID, fakeViewModel.currentEncounter.value?.visitId)
    }

    @Test
    fun arrivalScreen_arrivalMethodDropdown_updatesViewModel() {
        composeTestRule.onNodeWithTag("arrivalMethodDropdown").performClick()
        composeTestRule.onNodeWithText("Method 1").performClick()

        assertEquals("Method 1", fakeViewModel.currentEncounter.value?.arrivalMethod)
    }
}