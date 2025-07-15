package com.mgm.medimobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mgm.medimobile.ui.screens.dataentry.DataEntryScreen
import com.mgm.medimobile.ui.screens.encounterupdate.UpdateEncounterScreen
import com.mgm.medimobile.ui.screens.menus.EventSelectScreen
import com.mgm.medimobile.ui.screens.menus.LoginScreen
import com.mgm.medimobile.ui.screens.menus.MainMenuScreen
import com.mgm.medimobile.ui.screens.menus.SettingsScreen
import com.mgm.medimobile.viewmodel.MediMobileViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: MediMobileViewModel) {
    NavHost(
    navController = navController,
    startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController, viewModel) }
        composable("mainMenu") { MainMenuScreen(navController, viewModel) }
        composable("dataEntry") { DataEntryScreen(navController, viewModel) }
        composable("eventSelect") { EventSelectScreen(navController, viewModel) }
        composable("updateEncounter") { UpdateEncounterScreen(navController, viewModel) }
        composable("settings") { SettingsScreen(navController, viewModel) }
    }
}