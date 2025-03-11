package com.example.medimobile.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


@Composable
fun SettingsScreen(navController: NavController) {
    val expandedEventState = remember { mutableStateOf(false) }
    val expandedLocationState = remember { mutableStateOf(false) }
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
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
            ) {
                Text(text = "User Name", style = userNameTextStyle)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(text = "Settings", style = if (isLandscape) {
                    screenTitleTextLandscapeStyle
                } else {
                    screenTitleTextStyle
                } )

                Spacer(modifier = Modifier.weight(0.5f))

                val formSections = listOf(
                    FormSectionData("Event") {
                        Box(modifier = Modifier.wrapContentSize()) {
                            Column {
                                Button(onClick = { expandedEventState.value = !expandedEventState.value }) {
                                    Text(text = "Select Event")
                                }
                                DropdownMenu(
                                    expanded = expandedEventState.value,
                                    onDismissRequest = { expandedEventState.value = false }
                                ) {
                                    DropdownMenuItem(text = { Text("Shambhala") }, onClick = { expandedEventState.value = false })
                                }
                            }
                        }
                    },
                    FormSectionData("Location") {
                        Box(modifier = Modifier.wrapContentSize()) {
                            Column {
                                Button(onClick = { expandedLocationState.value = !expandedLocationState.value }) {
                                    Text(text = "Select Location")
                                }
                                DropdownMenu(
                                    expanded = expandedLocationState.value,
                                    onDismissRequest = { expandedLocationState.value = false }
                                ) {
                                    DropdownMenuItem(text = { Text("Main Medical") }, onClick = { expandedLocationState.value = false })
                                }
                            }
                        }
                    },
                )
                AdjustableFormFields(formSections = formSections, isLandscape = isLandscape)

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Back button at the bottom center
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Ensure it's at the bottom
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