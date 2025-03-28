package com.example.medimobile.ui.screens.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.ui.theme.appTitleTextStyle
import com.example.medimobile.ui.theme.userNameTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun MainMenuScreen(navController: NavController, viewModel: MediMobileViewModel) {

    val username = viewModel.currentUser.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding()
            .statusBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = username ?: "",
                    style = userNameTextStyle
                )
                Button(onClick = {
                    viewModel.logout()
                    navController.navigate("login")
                }) {
                    Text(text = "Logout")
                }
            }
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
                Button(onClick = {
                    viewModel.clearCurrentEncounter()
                    navController.navigate("dataEntry")
                }) {
                    Text(text = "Create New Encounter")
                }

                Spacer(modifier = Modifier.weight(0.25f))

                Button(onClick = {
                    viewModel.clearCurrentEncounter()
                    viewModel.loadEncountersFromDatabase()
                    navController.navigate("updateEncounter")
                }) {
                    Text(text = "Update Existing Encounter")
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}