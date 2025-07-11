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
import com.example.medimobile.testdata.TestData.FAKE_AGE
import com.example.medimobile.testdata.TestData.FAKE_COMMENT
import com.example.medimobile.ui.theme.MediMobileTheme
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TriageScreenTest {
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
                TriageScreen(viewModel = fakeViewModel)
            }
        }
    }

    @Test
    fun triageScreen_showsFields() {
        composeTestRule.onNodeWithTag("ageInputField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("roleDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("chiefComplaintDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("commentsTextField").assertIsDisplayed()
    }

    @Test
    fun triageScreen_ageInputField_updatesViewModel() {
        composeTestRule.onNodeWithTag("ageInputField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("ageInputField")
            .performTextInput(FAKE_AGE)

        assertEquals(FAKE_AGE.toInt(), fakeViewModel.currentEncounter.value?.age)
    }

    @Test
    fun triageScreen_roleDropdown_updatesViewModel() {
        composeTestRule.onNodeWithTag("roleDropdown").performClick()

        composeTestRule.onNodeWithText("Role 1").performClick()

        testScheduler.advanceUntilIdle()

        assertEquals("Role 1", fakeViewModel.currentEncounter.value?.role)
    }

    @Test
    fun triageScreen_chiefComplaintDropdown_updatesViewModel() {
        composeTestRule.onNodeWithTag("chiefComplaintDropdown").performClick()

        composeTestRule.onNodeWithText("Complaint 1").performClick()

        testScheduler.advanceUntilIdle()

        assertEquals("Complaint 1", fakeViewModel.currentEncounter.value?.chiefComplaint)
    }

    @Test
    fun triageScreen_commentsTextField_updatesViewModel() {
        composeTestRule.onNodeWithTag("commentsTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("commentsTextField")
            .performTextInput(FAKE_COMMENT)

        testScheduler.advanceUntilIdle()

        assertEquals(FAKE_COMMENT, fakeViewModel.currentEncounter.value?.comment)
    }
}
