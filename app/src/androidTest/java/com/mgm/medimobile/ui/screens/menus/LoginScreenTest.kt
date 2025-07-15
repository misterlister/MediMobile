package com.mgm.medimobile.ui.screens.menus

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mgm.medimobile.data.eventdata.EventList
import com.mgm.medimobile.fakes.FakeMediMobileViewModel
import com.mgm.medimobile.testdata.TestData.EVENT_NAME_2
import com.mgm.medimobile.testdata.TestData.GROUP_1_EVENT_1
import com.mgm.medimobile.testdata.TestData.GROUP_2_EVENT_2
import com.mgm.medimobile.testdata.TestData.INVALID_USERNAME
import com.mgm.medimobile.testdata.TestData.LOCATION_2_2
import com.mgm.medimobile.testdata.TestData.USERNAME_1
import com.mgm.medimobile.testdata.TestData.USERNAME_2
import com.mgm.medimobile.testdata.TestData.VALID_PASSWORD
import com.mgm.medimobile.testdata.TestData.mockEvents
import com.mgm.medimobile.ui.navigation.AppNavGraph
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var fakeViewModel: FakeMediMobileViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupClass() {
            EventList.EVENTS = mockEvents // override the events list before the testing
        }
    }

    @Before
    fun setup() {
        // Set up the TestNavHostController
        navController = TestNavHostController(composeTestRule.activity)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        // Create a fake ViewModel
        fakeViewModel = FakeMediMobileViewModel(dispatcher = testDispatcher)

        // Reinitialize the content for the test
        composeTestRule.setContent {
            AppNavGraph(
                navController = navController,
                viewModel = fakeViewModel
            )
        }
    }

    @Test
    fun loginScreenShowsAllMainContent() {
        composeTestRule.onNodeWithText("MediMobile").assertIsDisplayed()
        composeTestRule.onNodeWithTag("serviceDropdown").assertIsDisplayed()
        composeTestRule.onNodeWithTag("usernameTextField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("passwordTextField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loginButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("eventSelectButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loadingIndicator").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Login Failed").assertDoesNotExist()
    }

    @Test
    fun serviceDropdownAcceptsSelection() {
        composeTestRule.onNodeWithTag("serviceDropdown")
            .performClick()
        composeTestRule.onNodeWithText(GROUP_1_EVENT_1)
            .performClick()

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("serviceDropdown")
            .assertTextEquals(GROUP_1_EVENT_1)
    }

    @Test
    fun usernameFieldAcceptsInput() {
        composeTestRule.onNodeWithTag("usernameTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("usernameTextField")
            .performTextInput(USERNAME_1)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("usernameTextField")
            .assertTextEquals(USERNAME_1)
    }

    // No test for password input, since the password field has masked content
    // Login tests will verify that password input is working correctly

    @Test
    fun loggingInWorksWithValidInput() {
        composeTestRule.onNodeWithTag("serviceDropdown")
            .performClick()
        composeTestRule.onNodeWithText(GROUP_1_EVENT_1)
            .performClick()

        composeTestRule.onNodeWithTag("usernameTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("usernameTextField")
            .performTextInput(USERNAME_1)

        composeTestRule.onNodeWithTag("passwordTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("passwordTextField")
            .performTextInput(VALID_PASSWORD)

        composeTestRule.onNodeWithTag("loginButton")
            .performClick()

        testScheduler.advanceUntilIdle()

        composeTestRule.waitUntil(timeoutMillis = 3_000) {
            navController.currentDestination?.route == "mainMenu"
        }

        assertEquals("mainMenu", navController.currentDestination?.route)
    }

    @Test
    fun loggingInWorksWithDifferentValidInput() {
        composeTestRule.onNodeWithTag("eventSelectButton").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("eventDropdown").performClick()
        composeTestRule.onNodeWithText(EVENT_NAME_2).performClick()

        composeTestRule.onNodeWithTag("locationDropdown").performClick()
        composeTestRule.onNodeWithText(LOCATION_2_2).performClick()

        composeTestRule.onNodeWithTag("eventBackButton").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("serviceDropdown")
            .performClick()
        composeTestRule.onNodeWithText(GROUP_2_EVENT_2)
            .performClick()

        composeTestRule.onNodeWithTag("usernameTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("usernameTextField")
            .performTextInput(USERNAME_2)

        composeTestRule.onNodeWithTag("passwordTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("passwordTextField")
            .performTextInput(VALID_PASSWORD)

        composeTestRule.onNodeWithTag("loginButton").performClick()

        testScheduler.advanceUntilIdle()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            navController.currentDestination?.route == "mainMenu"
        }

        assertEquals("mainMenu", navController.currentDestination?.route)
    }


    @Test
    fun loggingInFailsWithInvalidUsername() {
        composeTestRule.onNodeWithTag("serviceDropdown").performClick()
        composeTestRule.onNodeWithText(GROUP_1_EVENT_1).performClick()

        composeTestRule.onNodeWithTag("usernameTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("usernameTextField")
            .performTextInput(INVALID_USERNAME)

        composeTestRule.onNodeWithTag("passwordTextField")
            .performTextClearance()
        composeTestRule.onNodeWithTag("passwordTextField")
            .performTextInput(VALID_PASSWORD)

        composeTestRule.onNodeWithTag("loginButton").performClick()

        testScheduler.advanceUntilIdle()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithText("Login Failed").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Login Failed").assertIsDisplayed()
        assertEquals("login", navController.currentDestination?.route)
    }

}