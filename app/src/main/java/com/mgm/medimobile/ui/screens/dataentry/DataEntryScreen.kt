package com.mgm.medimobile.ui.screens.dataentry

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.mgm.medimobile.R
import com.mgm.medimobile.data.model.StageStatus
import com.mgm.medimobile.data.model.getStatusColour
import com.mgm.medimobile.data.model.getStatusTextColour
import com.mgm.medimobile.data.remote.ApiResponseType
import com.mgm.medimobile.data.session.UserSessionManager
import com.mgm.medimobile.data.utils.isDataEmptyOrNull
import com.mgm.medimobile.ui.components.LoadingIndicator
import com.mgm.medimobile.ui.components.TopBarText
import com.mgm.medimobile.ui.components.UsernameText
import com.mgm.medimobile.ui.components.templates.ErrorPopup
import com.mgm.medimobile.ui.components.templates.MediButton
import com.mgm.medimobile.ui.components.templates.ScreenLayout
import com.mgm.medimobile.ui.theme.ButtonStatus
import com.mgm.medimobile.ui.theme.LocalExtendedColors
import com.mgm.medimobile.viewmodel.MediMobileViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun DataEntryScreen(navController: NavController, viewModel: MediMobileViewModel) {
    val currentEncounter = viewModel.currentEncounter.collectAsState().value
    val username = UserSessionManager.userDisplayName
    val encounter = viewModel.currentEncounter.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    // Initialize the current encounter if it's null
    if (currentEncounter == null) {
        viewModel.initNewEncounter()
    }

    val context = LocalContext.current

    // State for the cancellation pop-up
    var showCancelPopup by remember { mutableStateOf(false) }

    // State for the save pop-up
    var showSavePopup by remember { mutableStateOf(false) }

    // State for error pop-up
    val lifecycleOwner = LocalLifecycleOwner.current
    var showErrorPopup by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.errorFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { error ->
                errorText = error
                showErrorPopup = true
            }
    }

    LaunchedEffect(Unit) {
        viewModel.logoutEvent
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect {
                // Wait for error dialogs to close before logging out
                snapshotFlow { showErrorPopup }
                    .first { !it }
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
    }

    // Navigation tabs
    val tabs = listOf("Arrival", "Triage", "Discharge")
    val selectedTabIndex = remember { mutableIntStateOf(0) }

    // Tab colours based on stage status
    val arrivalStatusColour = getStatusColour(currentEncounter?.arrivalStatus)
    val triageStatusColour = getStatusColour(currentEncounter?.triageStatus)
    val dischargeStatusColour = getStatusColour(currentEncounter?.dischargeStatus)

    // Tab text colours based on stage status
    val arrivalStatusTextColour = getStatusTextColour(currentEncounter?.arrivalStatus)
    val triageStatusTextColour = getStatusTextColour(currentEncounter?.triageStatus)
    val dischargeStatusTextColour = getStatusTextColour(currentEncounter?.dischargeStatus)

    ScreenLayout(
        topBar = {
            // Username and DocID
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UsernameText(
                    text = username,
                    modifier = Modifier.testTag("usernameText")
                )
                Spacer(modifier = Modifier.width(8.dp))
                TopBarText(
                    text = if (isDataEmptyOrNull(encounter?.visitId)) {
                        stringResource(R.string.no_visit_id)
                    } else {
                        encounter!!.visitId
                    },
                    modifier = Modifier.testTag("visitIdText")

                )
            }
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {

                // Control Tabs to select between data entry stages

                TabRow(selectedTabIndex = selectedTabIndex.intValue) {
                    tabs.forEachIndexed { index, title ->
                        val tabColour = when (index) {
                            0 -> arrivalStatusColour
                            1 -> triageStatusColour
                            2 -> dischargeStatusColour
                            else -> LocalExtendedColors.current.medigrey.colorContainer
                        }

                        val tabTextColor = when (index) {
                            0 -> arrivalStatusTextColour
                            1 -> triageStatusTextColour
                            2 -> dischargeStatusTextColour
                            else -> LocalExtendedColors.current.medigrey.onColorContainer
                        }

                        val stageStatus = when (index) {
                            0 -> currentEncounter?.arrivalStatus
                            1 -> currentEncounter?.triageStatus
                            2 -> currentEncounter?.dischargeStatus
                            else -> StageStatus.NOT_STARTED
                        }

                        Tab(
                            selected = selectedTabIndex.intValue == index,
                            onClick = { selectedTabIndex.intValue = index },
                            modifier = Modifier.background(tabColour)
                                    .testTag("${title}Tab"),
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        color = tabTextColor,
                                        trackColor = tabColour,
                                        strokeWidth = 3.dp,
                                        progress = {
                                            when (stageStatus) {
                                                StageStatus.NOT_STARTED -> 0f
                                                StageStatus.IN_PROGRESS -> 0.5f
                                                StageStatus.COMPLETE -> 1f
                                                else -> 0f
                                            }
                                        }
                                    )

                                    Text(
                                        text = title,
                                        color = tabTextColor,
                                        fontWeight = if (selectedTabIndex.intValue == index) FontWeight.Bold else FontWeight.SemiBold,
                                        textDecoration = if (selectedTabIndex.intValue == index) TextDecoration.Underline else TextDecoration.None,
                                    )
                                }
                            }
                        )
                    }
                }

                // Entry stage content, based on currently selected tab

                Box(modifier = Modifier.fillMaxSize()) {
                    when (selectedTabIndex.intValue) {
                        0 -> ArrivalScreen(viewModel)
                        1 -> TriageScreen(viewModel)
                        2 -> DischargeScreen(viewModel)
                    }
                }
            }
        },
        bottomBar = {
            // **Button Section**
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                MediButton(
                    onClick = {
                        if (selectedTabIndex.intValue > 0) {
                            selectedTabIndex.intValue -= 1 // Go back one page
                        }
                    },
                    enabled = selectedTabIndex.intValue > 0,  // Disable when on first tab
                    modifier = Modifier.padding(start = 16.dp)  // Ensure it is 16.dp from the edge
                        .testTag("backButton")
                ) {
                    Text(text = "Prev")
                }

                // Cancel Button
                MediButton(
                    onClick = {
                        if (!viewModel.dataChanged.value) {
                            // If nothing is changed, just go back
                            navController.popBackStack()
                        } else {
                            // Otherwise show the cancel popup
                            showCancelPopup = true
                        }
                    },
                    status = ButtonStatus.WARNING,
                    modifier = Modifier.testTag("cancelButton")
                ) {
                    Text(text = "Cancel")
                }

                // Save Button
                MediButton(
                    onClick = {
                        if (currentEncounter == null) {
                            showErrorPopup = true
                            errorText = "Encounter not found"
                        } else if (!viewModel.dataChanged.value) {
                            showErrorPopup = true
                            errorText = "No changes to be saved"
                        } else if (currentEncounter.arrivalDate == null
                            || currentEncounter.arrivalTime == null) {
                            showErrorPopup = true
                            errorText = "Arrival Date and Time fields must be filled"
                        } else if (currentEncounter.visitId == "") {
                            showErrorPopup = true
                            errorText = "Visit ID field must be filled"
                        } else if (
                            currentEncounter.departureDate != null
                            && (currentEncounter.departureDate.isBefore(currentEncounter.arrivalDate)
                                || (currentEncounter.departureDate.isEqual(currentEncounter.arrivalDate)
                                    && currentEncounter.departureTime != null
                                    && currentEncounter.departureTime.isBefore(currentEncounter.arrivalTime)))
                        ) {
                            showErrorPopup = true
                            errorText = "Departure time must be after Arrival time"
                        } else {
                            showSavePopup = true
                        }
                    },
                    status = ButtonStatus.CONFIRM,
                    modifier = Modifier.testTag("saveButton")

                ) {
                    Text(text = "Save")
                }

                // Forward Button
                MediButton(
                    onClick = {
                        if (selectedTabIndex.intValue < tabs.size - 1) {
                            selectedTabIndex.intValue += 1 // Increment tab index
                        }
                    },
                    enabled = selectedTabIndex.intValue < tabs.size - 1, // Disable when on last tab
                    modifier = Modifier.padding(end = 16.dp)  // Ensure it is 16.dp from the edge
                        .testTag("forwardButton")
                ) {
                    Text(text = "Next")
                }
            }
        }
    )

    // **Confirm Cancel Popup**
    if (showCancelPopup) {
        AlertDialog(
            onDismissRequest = { showCancelPopup = false },
            title = { Text(text = "Are you sure you want to cancel?") },
            text = { Text("Any entered data will be lost.") },
            confirmButton = {
                MediButton(
                    onClick = {
                        // Go back to the previous page and discard current content
                        navController.popBackStack()
                        showCancelPopup = false
                    },
                    status = ButtonStatus.WARNING
                ) {
                    Text("Exit Without Saving")
                }
            },
            dismissButton = {
                MediButton(
                    onClick = {
                        // Dismiss the dialog if "No" is clicked
                        showCancelPopup = false
                    }
                ) {
                    Text("Return to Entry")
                }
            },
            modifier = Modifier.testTag("cancelPopup")
        )
    }

    if (showErrorPopup) {
        ErrorPopup(
            errorMessage = errorText,
            onDismiss = { showErrorPopup = false },
            modifier = Modifier.testTag("errorPopup")
        )
    }

    if (showSavePopup) {
        val coroutineScope = rememberCoroutineScope()

        AlertDialog(
            onDismissRequest = { showSavePopup = false },
            title = { Text(text = "Confirm save?") },
            text = { Text("Data will be submitted to the database.") },
            confirmButton = {
                MediButton(
                    onClick = {
                        // Save data to database and continue entry
                        coroutineScope.launch {
                            val responseResult = viewModel.saveEncounterToDatabase()
                            showSavePopup = false  // Close the save popup

                            if (responseResult == ApiResponseType.SUCCESS) {
                                // Show toast message on success
                                Toast.makeText(context, "Save successful!", Toast.LENGTH_SHORT)
                                    .show()
                            } else if (responseResult == ApiResponseType.LOGOUT) {
                                viewModel.logout()
                            }
                        }
                    },
                    status = ButtonStatus.CONFIRM,
                    modifier = Modifier.testTag("confirmSaveAndContinueButton")
                ) {
                    Text("Save & Continue")
                }

                MediButton(
                    onClick = {
                        coroutineScope.launch {
                            val responseResult = viewModel.saveEncounterToDatabase()
                            showSavePopup = false  // Close the save popup

                            if (responseResult == ApiResponseType.SUCCESS) {
                                // Navigate back on successful save
                                navController.popBackStack()
                                // Show toast message on success
                                Toast.makeText(context, "Save successful!", Toast.LENGTH_SHORT).show()
                            } else if (responseResult == ApiResponseType.LOGOUT) {
                                viewModel.logout()
                            }
                        }
                    },
                    status = ButtonStatus.CONFIRM,
                    modifier = Modifier.testTag("confirmSaveAndExitButton")
                ) {
                    Text("Save & Exit")
                }
            },
            dismissButton = {
                MediButton(
                    onClick = {
                        // Dismiss the dialog if "No" is clicked
                        showSavePopup = false
                    },
                    status = ButtonStatus.WARNING,
                    modifier = Modifier.testTag("cancelSaveButton")
                ) {
                    Text("No")
                }
            },
            modifier = Modifier.testTag("savePopup")
        )
    }

    // Freeze screen and show loading indicator when loading
    LoadingIndicator(isLoading)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DataEntryScreenPreview() {
    DataEntryScreen(navController = NavController(LocalContext.current), viewModel = MediMobileViewModel())
}