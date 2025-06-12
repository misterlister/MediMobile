package com.example.medimobile.ui.screens.dataentry

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.medimobile.fakes.FakeMediMobileViewModel
import com.example.medimobile.testdata.TestData.FAKE_VISIT_ID
import com.example.medimobile.ui.navigation.AppNavGraph
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DataEntryScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var fakeViewModel: FakeMediMobileViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setup() {
        // Set up the TestNavHostController
        navController = TestNavHostController(composeTestRule.activity)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        // Create a fake ViewModel
        fakeViewModel = FakeMediMobileViewModel(dispatcher = testDispatcher)

        composeTestRule.setContent {
            AppNavGraph(navController = navController, viewModel = fakeViewModel)
        }

        // Set the current destination to the Data Entry screen before each test
        composeTestRule.runOnIdle {
            navController.navigate("dataEntry")
        }
    }

    @Test
    fun dataEntryScreen_showsBarsAndTabs() {
        composeTestRule.onNodeWithTag("usernameText").assertIsDisplayed()
        composeTestRule.onNodeWithTag("visitIdText").assertIsDisplayed()
        composeTestRule.onNodeWithTag("TriageTab").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Information CollectionTab").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DischargeTab").assertIsDisplayed()
        composeTestRule.onNodeWithTag("backButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("cancelButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("saveButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("forwardButton").assertIsDisplayed()
    }

    @Test
    fun dataEntryScreen_doesNotShowPopupsOnLoad() {
        composeTestRule.onNodeWithTag("cancelPopup").assertDoesNotExist()
        composeTestRule.onNodeWithTag("savePopup").assertDoesNotExist()
        composeTestRule.onNodeWithTag("errorPopup").assertDoesNotExist()
    }

    @Test
    fun dataEntryScreen_tabSwitchingDisplaysCorrectScreens() {
        // Confirm that the Triage screen is displayed by default
        composeTestRule.onNodeWithTag("visitIdTextField").assertIsDisplayed()

        // Switch to the Information Collection screen, and confirm that it's displayed
        composeTestRule.onNodeWithTag("Information CollectionTab").performClick()
        composeTestRule.onNodeWithTag("ageInputField").assertIsDisplayed()

        // Switch to the Discharge screen, and confirm that it's displayed
        composeTestRule.onNodeWithTag("DischargeTab").performClick()
        composeTestRule.onNodeWithTag("departureDestinationDropdown").assertIsDisplayed()

        // Switch back to the Triage screen, and confirm that it's displayed again
        composeTestRule.onNodeWithTag("TriageTab").performClick()
        composeTestRule.onNodeWithTag("visitIdTextField").assertIsDisplayed()
    }

    @Test
    fun dataEntryScreen_saveWithoutChanges_showsErrorPopup() {
        composeTestRule.onNodeWithTag("saveButton").performClick()
        composeTestRule.onNodeWithTag("errorPopup").assertIsDisplayed()
        composeTestRule.onNodeWithTag("errorPopupText")
            .assertTextEquals("No changes to be saved")
    }

    @Test
    fun dataEntryScreen_cancelWithoutChanges_doesNotShowPopup() {
        composeTestRule.onNodeWithTag("cancelButton").performClick()
        composeTestRule.onNodeWithTag("cancelPopup").assertDoesNotExist()
    }

    @Test
    fun dataEntryScreen_cancelWithChanges_showsCancelPopup() {
        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextInput(FAKE_VISIT_ID)
        composeTestRule.onNodeWithTag("cancelButton").performClick()
        composeTestRule.onNodeWithTag("cancelPopup").assertIsDisplayed()
    }

    @Test
    fun dataEntryScreen_saveFailsWithNoArrivalTime() {
        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextInput(FAKE_VISIT_ID)

        composeTestRule.onNodeWithTag("saveButton").performClick()
        composeTestRule.onNodeWithTag("errorPopup").assertIsDisplayed()
        composeTestRule.onNodeWithTag("errorPopupText")
            .assertTextEquals("Arrival Date and Time fields must be filled")
    }

    @Test
    fun dataEntryScreen_saveFailsWithEmptyVisitId() {
        composeTestRule.onNodeWithTag("nowDateTimeButton").performClick()

        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextClearance()

        composeTestRule.onNodeWithTag("saveButton").performClick()
        composeTestRule.onNodeWithTag("errorPopup").assertIsDisplayed()
        composeTestRule.onNodeWithTag("errorPopupText")
            .assertTextEquals("Visit ID field must be filled")
    }

    @Test
    fun dataEntryScreen_saveSuccess() {
        composeTestRule.onNodeWithTag("nowDateTimeButton").performClick()
        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("visitIdTextField")
            .performTextInput(FAKE_VISIT_ID)

        composeTestRule.onNodeWithTag("saveButton").performClick()
        composeTestRule.onNodeWithTag("savePopup").assertIsDisplayed()

        composeTestRule.onNodeWithTag("confirmSaveAndContinueButton").performClick()

        testScheduler.advanceUntilIdle()
        assertTrue(fakeViewModel.saveSuccess)
    }
}