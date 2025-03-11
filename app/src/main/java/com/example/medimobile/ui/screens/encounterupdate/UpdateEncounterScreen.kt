package com.example.medimobile.ui.screens.encounterupdate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.ui.components.templates.AdjustableFormFields
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.theme.screenTitleTextLandscapeStyle
import com.example.medimobile.ui.theme.screenTitleTextStyle
import com.example.medimobile.ui.theme.userNameTextStyle
import getDummyEncounters

@Composable
fun UpdateEncounterScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "User Name", style = userNameTextStyle)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                Text(text = "Update Encounter", style = if (isLandscape) {
                    screenTitleTextLandscapeStyle
                } else {
                    screenTitleTextStyle
                } )

                val formSections = listOf(
                    FormSectionData(title = null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(onClick = { /*TODO*/ }) {
                                Text(text = "Refresh Table")
                            }

                            Box(
                                modifier = Modifier
                                    .heightIn(max = if (isLandscape) 180.dp else 280.dp)
                                    .fillMaxWidth()
                            ) {
                                EncounterTable(
                                    records = getDummyEncounters(),
                                    onRowClick = { selectedRecord ->
                                        println("Selected Encounter: ${selectedRecord.documentNum}")
                                    }
                                )
                            }
                        }
                    },
                    FormSectionData(title = null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(onClick = { /*TODO*/ }) {
                                Text(text = "Open with QR Scan")
                            }

                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextField(
                                    value = "",
                                    onValueChange = {},
                                    placeholder = { Text("Doc ID") },
                                    modifier = Modifier.fillMaxWidth(0.6f))

                                Spacer(modifier = Modifier.width(16.dp))

                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "Open")
                                }
                            }

                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextField(
                                    value = "",
                                    onValueChange = {},
                                    placeholder = { Text("VisitID") },
                                    modifier = Modifier.fillMaxWidth(0.6f))

                                Spacer(modifier = Modifier.width(16.dp))

                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "Open")
                                }
                            }
                        }
                    },
                )

                AdjustableFormFields(formSections = formSections, isLandscape = isLandscape)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.LightGray)
                .padding(16.dp)
                .navigationBarsPadding()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { navController.navigate("mainMenu") },
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                Text(text = "Back", color = Color.Black)
            }
        }
    }
}