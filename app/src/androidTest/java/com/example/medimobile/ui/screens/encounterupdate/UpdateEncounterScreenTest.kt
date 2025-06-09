package com.example.medimobile.ui.screens.encounterupdate

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medimobile.fakes.FakeMediMobileViewModel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UpdateEncounterScreenTest {

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

        // Reinitialize the content for the test
        composeTestRule.setContent {
            UpdateEncounterScreen(
                navController = navController,
                viewModel = fakeViewModel
            )
        }
    }

    @Test
    fun updateEncounterScreen_showsMainUIElements() {
        composeTestRule.onNodeWithTag("refreshButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("qrButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("dateFilterButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("clearDateFilterButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("visitIdFilter").assertIsDisplayed()
        composeTestRule.onNodeWithTag("clearVisitIdFilterButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("showCompletedCheckbox").assertIsDisplayed()
        composeTestRule.onNodeWithTag("updateEncounterBackButton").assertIsDisplayed()
    }


    @Test
    fun updateEncounterScreen_showsEncounterTableOrEmptyState() {
        val encounterTableNode = composeTestRule.onAllNodesWithTag("encounterTable").fetchSemanticsNodes()
        val emptyTableNode = composeTestRule.onAllNodesWithTag("emptyEncounterTable").fetchSemanticsNodes()

        if (encounterTableNode.isNotEmpty()) {
            composeTestRule.onNodeWithTag("encounterTable").assertIsDisplayed()
        } else if (emptyTableNode.isNotEmpty()) {
            composeTestRule.onNodeWithTag("emptyEncounterTable").assertIsDisplayed()
        } else {
            fail("Neither encounter table nor empty state found")
        }
    }
}