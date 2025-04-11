package com.example.medimobile.ui.screens.dataentry

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.model.getStatusColour
import com.example.medimobile.ui.components.LoadingIndicator
import com.example.medimobile.ui.theme.userNameTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun DataEntryScreen(navController: NavController, viewModel: MediMobileViewModel) {
    val focusManager = LocalFocusManager.current
    val currentEncounter = viewModel.currentEncounter.collectAsState().value
    val username = viewModel.currentUser.collectAsState().value
    val encounter = viewModel.currentEncounter.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    // Initialize the current encounter if it's null
    if (currentEncounter == null) {
        viewModel.setCurrentEncounter(PatientEncounter())
        viewModel.setCurrentUser(viewModel.currentUser.value ?: "")
        viewModel.setLocation(viewModel.selectedLocation.value ?: "")
    }

    // State for the cancellation pop-up
    var showCancelPopup by remember { mutableStateOf(false) }

    // State for the save pop-up
    var showSavePopup by remember { mutableStateOf(false) }

    // State for error pop-up
    var showErrorPopup by remember { mutableStateOf(false) }

    var errorText by remember { mutableStateOf("") }

    // Navigation tabs
    val tabs = listOf("Triage", "Information Collection", "Discharge")
    val selectedTabIndex = remember { mutableIntStateOf(0) }

    // Tab Colours based on stage status
    val triageStatusColour = getStatusColour(currentEncounter?.triageStatus)
    val informationCollectionStatusColour = getStatusColour(currentEncounter?.informationCollectionStatus)
    val dischargeStatusColour = getStatusColour(currentEncounter?.dischargeStatus)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Clears focus when tapping outside of fields
                        focusManager.clearFocus()
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // **Username, DocID, and Tabs Section**
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                // Username and DocID
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = username ?: "User",
                        style = userNameTextStyle
                    )
                    Text(
                        text = encounter?.visitId ?: "Visit ID not set",
                        style = userNameTextStyle
                    )
                }

                // Tab selection
                TabRow(
                    selectedTabIndex = selectedTabIndex.intValue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        val tabColour = when (index) {
                            0 -> triageStatusColour
                            1 -> informationCollectionStatusColour
                            2 -> dischargeStatusColour
                            else -> Color.LightGray
                        }
                        Tab(
                            selected = selectedTabIndex.intValue == index,
                            onClick = { selectedTabIndex.intValue = index },
                            text = { Text(text = title) },
                            modifier = Modifier.background(tabColour)
                        )
                    }
                }
            }

            // **Main Content Section**
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTabIndex.intValue) {
                    0 -> TriageScreen(viewModel)
                    1 -> InformationCollectionScreen(viewModel)
                    2 -> DischargeScreen(viewModel)
                }
            }

            // **Button Section**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(12.dp)
                    .navigationBarsPadding()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                Button(
                    onClick = {
                        if (selectedTabIndex.intValue > 0) {
                            selectedTabIndex.intValue -= 1 // Go back one page
                        }
                    },
                    enabled = selectedTabIndex.intValue > 0,  // Disable when index is 0
                    modifier = Modifier.padding(start = 16.dp)  // Ensure it is 16.dp from the edge
                ) {
                    Text(text = "Prev")
                }

                // Cancel Button
                Button(
                    onClick = { showCancelPopup = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Cancel")
                }

                // Save Button
                Button(
                    onClick = {
                        if (currentEncounter == null) {
                            showErrorPopup = true
                            errorText = "Encounter not found"
                        } else if (currentEncounter.visitId == "") {
                            showErrorPopup = true
                            errorText = "Visit ID field must be filled"
                        } else {
                            showSavePopup = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Save")
                }

                // Forward Button
                Button(
                    onClick = {
                        if (selectedTabIndex.intValue < tabs.size - 1) {
                            selectedTabIndex.intValue += 1 // Increment tab index
                        }
                    },
                    enabled = selectedTabIndex.intValue < tabs.size - 1,  // Disable when index is at max
                    modifier = Modifier.padding(end = 16.dp)  // Ensure it is 16.dp from the edge
                ) {
                    Text(text = "Next")
                }
            }
        }
    }

    // Freeze screen and show loading indicator when loading
    LoadingIndicator(isLoading)

    // **Confirm Cancel Popup**
    if (showCancelPopup) {
        AlertDialog(
            onDismissRequest = { showCancelPopup = false },
            title = { Text(text = "Are you sure you want to cancel?") },
            text = { Text("Any entered data will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        // Go back to the previous page and discard current content
                        navController.popBackStack()
                        showCancelPopup = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Dismiss the dialog if "No" is clicked
                        showCancelPopup = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }

    // **Error Popup**
    if (showErrorPopup) {
        AlertDialog(
            onDismissRequest = { showErrorPopup = false },
            title = { Text(text = "Error") },
            text = { Text(errorText) },
            confirmButton = {
                Button(
                    onClick = {
                        // Dismiss the dialog
                        showErrorPopup = false
                    }
                ) {
                    Text("Ok")
                }
            },
        )
    }

    if (showSavePopup) {
        AlertDialog(
            onDismissRequest = { showSavePopup = false },
            title = { Text(text = "Confirm save?") },
            text = { Text("Data will be submitted to the database.") },
            confirmButton = {
                Button(
                    onClick = {
                        // Save data to database and continue entry
                        viewModel.saveEncounterToDatabase()
                        showSavePopup = false
                    }
                ) {
                    Text("Save & Continue")
                }

                Button(
                    onClick = {
                        // Save data to database and go back to the previous page
                        viewModel.saveEncounterToDatabase()
                        navController.popBackStack()
                        showSavePopup = false
                    }
                ) {
                    Text("Save & Exit")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Dismiss the dialog if "No" is clicked
                        showSavePopup = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}
