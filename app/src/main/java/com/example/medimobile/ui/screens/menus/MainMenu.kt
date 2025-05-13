package com.example.medimobile.ui.screens.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.data.constants.UIConstants.NO_USER
import com.example.medimobile.ui.components.templates.MediButton
import com.example.medimobile.ui.components.templates.ScreenLayout
import com.example.medimobile.ui.theme.appTitleTextStyle
import com.example.medimobile.ui.theme.userNameTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun MainMenuScreen(navController: NavController, viewModel: MediMobileViewModel) {

    val username = viewModel.currentUser.collectAsState().value

    ScreenLayout(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = username ?: NO_USER,
                    style = userNameTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                MediButton(onClick = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("mainMenu") { inclusive = true }
                    }
                }) {
                    Text(text = "Logout")
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    text = "MediMobile",
                    style = appTitleTextStyle
                )

                Spacer(modifier = Modifier.weight(0.5f))

                // Menu Options
                MediButton(onClick = {
                    viewModel.clearCurrentEncounter()
                    navController.navigate("dataEntry")
                }) {
                    Text(text = "Create New Encounter")
                }

                Spacer(modifier = Modifier.weight(0.25f))

                MediButton(onClick = {
                    viewModel.clearCurrentEncounter()
                    viewModel.loadEncountersFromDatabase()
                    navController.navigate("updateEncounter")
                }) {
                    Text(text = "Update Existing Encounter")
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MediButton(
                    onClick = { navController.navigate("settings") },
                ) {
                    Text(text = "Settings")
                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainMenuScreenPreview() {
    MainMenuScreen(navController = NavController(LocalContext.current), viewModel = MediMobileViewModel())
}