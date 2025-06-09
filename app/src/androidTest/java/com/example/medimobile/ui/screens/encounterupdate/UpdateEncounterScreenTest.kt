package com.example.medimobile.ui.screens.encounterupdate

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medimobile.fakes.FakeMediMobileViewModel
import com.example.medimobile.testdata.TestData.mockEncounters
import com.example.medimobile.ui.navigation.AppNavGraph
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

        composeTestRule.setContent {
            AppNavGraph(navController = navController, viewModel = fakeViewModel)
        }

        // Set the current destination to the update encounter screen before each test
        composeTestRule.runOnIdle {
            navController.navigate("updateEncounter")
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

    @Test
    fun backButton_navigatesToMainMenu() {

        composeTestRule.onNodeWithTag("updateEncounterBackButton").performClick()
        testScheduler.advanceUntilIdle()
        composeTestRule.waitUntil(timeoutMillis = 3_000) {
            navController.currentDestination?.route == "mainMenu"
        }
        assertEquals("mainMenu", navController.currentDestination?.route)
    }

    @Test
    fun refreshButton_populatesEncounterTable() {
        // Ensure the table is initially empty
        composeTestRule.onNodeWithTag("emptyEncounterTable").assertIsDisplayed()

        // Click the refresh button
        composeTestRule.onNodeWithTag("refreshButton").performClick()

        // Advance until the UI is idle (for recomposition + state update)
        testScheduler.advanceUntilIdle()

        // Verify the encounter table is now displayed
        composeTestRule.waitUntil(timeoutMillis = 3_000) {
            composeTestRule.onAllNodesWithTag("encounterTable").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("encounterTable").assertIsDisplayed()
    }

    @Test
    fun visitIdFilter_displaysOnlyMatchingEncounter() {
        composeTestRule.onNodeWithTag("visitIdFilter").performTextInput("GEV1L1125-00001")
        composeTestRule.onNodeWithTag("refreshButton").performClick()
        testScheduler.advanceUntilIdle()

        val rows = composeTestRule.onAllNodesWithTag("encounterTableRow").fetchSemanticsNodes()
        assertEquals(1, rows.size)

        val filteredNodes = composeTestRule.onAllNodesWithTag("encounterTableRow")
            .filter(hasText("GEV1L1125-00001"))

        assertTrue("No row found with matching VisitID", filteredNodes.fetchSemanticsNodes().isNotEmpty())

        filteredNodes[0].assertIsDisplayed()
    }

    @Test
    fun showCompletedCheckbox_togglesCompletedEncounterVisibility() {
        val expectedIncomplete = mockEncounters.count { !it.complete }
        val expectedAll = mockEncounters.size

        // Refresh
        composeTestRule.onNodeWithTag("refreshButton").performClick()
        testScheduler.advanceUntilIdle()

        // Only incomplete shown by default
        var rows = composeTestRule.onAllNodesWithTag("encounterTableRow").fetchSemanticsNodes()
        assertEquals(expectedIncomplete, rows.size)

        // Show all the nodes, make sure they are all there
        composeTestRule.onNodeWithTag("showCompletedCheckbox").performClick()
        testScheduler.advanceUntilIdle()
        rows = composeTestRule.onAllNodesWithTag("encounterTableRow").fetchSemanticsNodes()
        assertEquals(expectedAll, rows.size)

        // Hide the completed nodes, make sure they are all gone
        composeTestRule.onNodeWithTag("showCompletedCheckbox").performClick()
        testScheduler.advanceUntilIdle()
        rows = composeTestRule.onAllNodesWithTag("encounterTableRow").fetchSemanticsNodes()
        assertEquals(expectedIncomplete, rows.size)
    }




}