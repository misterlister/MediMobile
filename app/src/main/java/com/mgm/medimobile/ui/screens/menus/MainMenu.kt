package com.mgm.medimobile.ui.screens.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.mgm.medimobile.R
import com.mgm.medimobile.data.session.UserSessionManager
import com.mgm.medimobile.ui.components.AppTitle
import com.mgm.medimobile.ui.components.UsernameText
import com.mgm.medimobile.ui.components.templates.MediButton
import com.mgm.medimobile.ui.components.templates.ScreenLayout
import com.mgm.medimobile.viewmodel.MediMobileViewModel

@Composable
fun MainMenuScreen(navController: NavController, viewModel: MediMobileViewModel) {

    val username = UserSessionManager.userDisplayName
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.logoutEvent
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
    }

    ScreenLayout(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UsernameText(text = username)
                MediButton(onClick = {
                    viewModel.logout()
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

                AppTitle()

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
                    Text(text = stringResource(R.string.settings_title))
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