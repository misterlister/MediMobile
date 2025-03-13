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
import com.example.medimobile.ui.theme.userNameTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun DataEntryScreen(navController: NavController, viewModel: MediMobileViewModel) {
    val focusManager = LocalFocusManager.current
    val currentEncounter = viewModel.currentEncounter.collectAsState().value
    val username = viewModel.currentUser.collectAsState().value

    // Initialize the current encounter if it's null
    if (currentEncounter == null) {
        viewModel.setCurrentEncounter(PatientEncounter())
    }

    // State for the cancellation pop-up
    var showCancelPopup by remember { mutableStateOf(false) }

    // Navigation tabs
    val tabs = listOf("Triage", "Information Collection", "Discharge")
    val selectedTabIndex = remember { mutableIntStateOf(0) }

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
            modifier = Modifier.fillMaxSize()
        ) {
            // **Username, DocID, and Tabs Section**
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .statusBarsPadding()
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
                        text = "Doc ID",
                        style = userNameTextStyle
                    )
                }

                // Tab selection
                TabRow(
                    selectedTabIndex = selectedTabIndex.intValue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex.intValue == index,
                            onClick = { selectedTabIndex.intValue = index },
                            text = { Text(text = title) }
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
                    .padding(16.dp)
                    .navigationBarsPadding()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { showCancelPopup = true }) {
                    Text(text = "Cancel", color = Color.Black)
                }
                Button(onClick = { navController.navigate("mainMenu")  }) {
                    Text(text = "Save", color = Color.Black)
                }
            }
        }
    }
    // **Confirm Cancel Popup**
    if (showCancelPopup) {
        AlertDialog(
            onDismissRequest = { showCancelPopup = false },
            title = { Text(text = "Are you sure you want to cancel?") },
            text = { Text("Any entered data will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        // Navigate to main menu and discard the current entry if "Yes" is clicked
                        viewModel.clearCurrentEncounter()
                        navController.navigate("mainMenu")
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
}
