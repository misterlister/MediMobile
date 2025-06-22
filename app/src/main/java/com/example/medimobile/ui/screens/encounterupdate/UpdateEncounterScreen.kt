package com.example.medimobile.ui.screens.encounterupdate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.example.medimobile.data.constants.UIConstants.NO_USER
import com.example.medimobile.data.utils.dateFormatter
import com.example.medimobile.data.utils.toDisplayValue
import com.example.medimobile.ui.components.LoadingIndicator
import com.example.medimobile.ui.components.ScreenTitle
import com.example.medimobile.ui.components.UsernameText
import com.example.medimobile.ui.components.inputfields.DateSelector
import com.example.medimobile.ui.components.inputfields.QRScannerButton
import com.example.medimobile.ui.components.templates.ErrorPopup
import com.example.medimobile.ui.components.templates.MediButton
import com.example.medimobile.ui.components.templates.MediTextField
import com.example.medimobile.ui.components.templates.ScreenLayout
import com.example.medimobile.ui.theme.ButtonStatus
import com.example.medimobile.ui.theme.placeholderTextStyle
import com.example.medimobile.ui.theme.screenTitleTextStyle
import com.example.medimobile.ui.theme.userNameTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel
import java.time.LocalDate

@Composable
fun UpdateEncounterScreen(navController: NavController, viewModel: MediMobileViewModel) {
    // Reference to viewModel's encounterList
    val encounterList by viewModel.encounterList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState().value

    val username = viewModel.currentUser.collectAsState().value

    // State used to trigger the alert pop-up
    val showNotFoundDialog = remember { mutableStateOf(false) }

    // State for error pop-up
    val lifecycleOwner = LocalLifecycleOwner.current
    var showErrorPopup by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    val selectedRange by viewModel.selectedDateRange.collectAsState()

    val displayRangeText = "Display Range: " + selectedRange.toDisplayValue()


    LaunchedEffect(Unit) {
        viewModel.errorFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { error ->
                errorText = error
                showErrorPopup = true
            }
    }

    // State to hold values used in the alert pop-up
    val alertValue = remember { mutableStateOf("") }

    var showDateDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // States for filter values
    val dateFilter = remember { mutableStateOf<LocalDate?>(null) }
    val visitIdFilter = remember { mutableStateOf("") }
    val showCompleted = remember { mutableStateOf(false) } // Default to false

    val filteredEncounterList = remember {
        derivedStateOf {
            encounterList.filter { encounter ->

                    val isCompleted = showCompleted.value || !encounter.complete
                    val isVisitIdMatch = visitIdFilter.value.isEmpty() || encounter.visitId.contains(visitIdFilter.value, ignoreCase = true)
                    val isDateMatch = dateFilter.value == null || encounter.arrivalDate == dateFilter.value
                    isCompleted && isDateMatch && isVisitIdMatch
                }
        }
    }

    val focusManager = LocalFocusManager.current

    ScreenLayout(
        topBar = {
            UsernameText(text = username)
        },
        content = {
            // **Table Section**
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // **Title**
                ScreenTitle(
                    text = "Update Encounter",
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // **Refresh Button**
                MediButton(
                    onClick = { viewModel.loadEncountersFromDatabase() },
                    modifier = Modifier.testTag("refreshButton")
                ) {
                    Text(text = "Refresh Table")
                }

                Text(
                    text = displayRangeText,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                ) {
                    EncounterTable(
                        records = filteredEncounterList.value,
                        onRowClick = { selectedRecord ->
                            // Load the selected record into the viewModel
                            viewModel.setCurrentEncounter(selectedRecord)
                            // Ensure that the record is marked as submitted
                            viewModel.markAsSubmitted()
                            navController.navigate("dataEntry")
                        },
                        isLoading = isLoading,
                        modifier = Modifier.testTag("encounterTable")
                    )
                }

                // **Lookup and Filter Section**

                QRScannerButton(
                    onResult = { scannedValue ->
                        val encounter = viewModel.findEncounterByVisitId(scannedValue)

                        if (encounter != null) {
                            viewModel.setCurrentEncounter(encounter)
                            navController.navigate("dataEntry")
                        } else {
                            // Show a pop-up to tell the user that no Encounter matches that ID
                            alertValue.value = scannedValue
                            showNotFoundDialog.value = true
                        }
                    },
                    modifier = Modifier.testTag("qrButton")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    MediButton(
                        onClick = { showDateDialog = true },
                        modifier = Modifier.testTag("dateFilterButton")
                    ) {
                        Text(text = dateFilter.value?.format(dateFormatter) ?: "Filter by Date")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    MediButton(
                        onClick = { dateFilter.value = null },
                        status = ButtonStatus.WARNING,
                        enabled = dateFilter.value != null, // Disable when no date is selected
                        modifier = Modifier.testTag("clearDateFilterButton")
                    ) {
                        Text(text = "X")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MediTextField(
                        value = visitIdFilter.value,
                        onValueChange = { visitIdFilter.value = it },
                        placeholder = { Text("Filter by Visit ID", style = placeholderTextStyle) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .testTag("visitIdFilter")
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    MediButton(
                        onClick = { visitIdFilter.value = "" },
                        status = ButtonStatus.WARNING,
                        enabled = visitIdFilter.value.isNotEmpty(), // Disable when no visit ID is entered
                        modifier = Modifier.testTag("clearVisitIdFilterButton")
                    ) {
                        Text(text = "X")
                    }
                }

                // **Show Completed Filter**
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Completed:",
                        style = TextStyle(fontWeight = FontWeight.Bold))
                    Checkbox(
                        checked = showCompleted.value,
                        onCheckedChange = { showCompleted.value = it },
                        modifier = Modifier.testTag("showCompletedCheckbox")
                    )
                }
            }
        },
        bottomBar = {
            MediButton(
                onClick = { navController.navigate("mainMenu") },
                modifier = Modifier.testTag("updateEncounterBackButton")
            ) {
                Text(text = "Back")
            }
        }
    )

    if (showErrorPopup) {
        ErrorPopup(
            errorMessage = errorText,
            onDismiss = { showErrorPopup = false }
        )
    }

    // Show pop-up dialog if no encounter matches a given VisitID
    if (showNotFoundDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when clicking outside
                showNotFoundDialog.value = false
                alertValue.value = ""
            },
            title = { Text("No Encounter Found") },
            text = {
                Text("No Encounter matches visit ID: ${alertValue.value}")
            },
            confirmButton = {
                MediButton(
                    onClick = {
                        // Dismiss the dialog when clicking OK
                        showNotFoundDialog.value = false
                        alertValue.value = ""
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
    if (showDateDialog) {
        DateSelector (
            context = context,
            date = dateFilter.value,
            onDateSelected = { newDate ->
                dateFilter.value = newDate
                showDateDialog = false
            },
            onDismiss = { showDateDialog = false }
        )
    }

    // Freeze screen and show loading indicator when loading
    LoadingIndicator(isLoading)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UpdateEncounterScreenPreview() {
    UpdateEncounterScreen(navController = NavController(LocalContext.current), viewModel = MediMobileViewModel())
}