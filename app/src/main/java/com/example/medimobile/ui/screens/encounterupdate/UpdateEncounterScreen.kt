package com.example.medimobile.ui.screens.encounterupdate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.ui.components.inputfields.QRScannerButton
import com.example.medimobile.ui.theme.placeholderTextStyle
import com.example.medimobile.ui.theme.screenTitleTextStyle
import com.example.medimobile.ui.theme.userNameTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun UpdateEncounterScreen(navController: NavController, viewModel: MediMobileViewModel) {
    // Reference to viewModel's encounterList
    val encounterList by viewModel.encounterList.collectAsState()

    // State used to trigger the alert pop-up
    val showNotFoundDialog = remember { mutableStateOf(false) }

    // State to hold values used in the alert pop-up
    val alertValue = remember { mutableStateOf("") }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // **Username Section**
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "User Name", style = userNameTextStyle)
            }

            // **Main Content Section**
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // **Table Section**
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // **Title**
                    Text(text = "Update Encounter", style = screenTitleTextStyle)

                    Spacer(modifier = Modifier.height(16.dp))

                    // **Refresh Button**
                    Button(onClick = { viewModel.loadEncountersFromDatabase() }) {
                        Text(text = "Refresh Table")
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp)
                    ) {
                        EncounterTable(
                            records = encounterList,
                            onRowClick = { selectedRecord ->
                                viewModel.setCurrentEncounter(selectedRecord)
                                navController.navigate("dataEntry")
                            }
                        )
                    }

                    // **Explicit Lookup Section**

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
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Doc ID", style = placeholderTextStyle) },
                            modifier = Modifier.fillMaxWidth(0.6f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Open")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Visit ID", style = placeholderTextStyle) },
                            modifier = Modifier.fillMaxWidth(0.6f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Open")
                        }
                    }
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
                Button(
                    onClick = { navController.navigate("mainMenu") },
                    modifier = Modifier
                ) {
                    Text(text = "Back", color = Color.Black)
                }
            }
        }
    }
    // Show pop-up dialog if no encounter is found
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
                Button(
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UpdateEncounterScreenPreview() {
    UpdateEncounterScreen(navController = NavController(LocalContext.current), viewModel = MediMobileViewModel())
}