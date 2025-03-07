package com.example.medimobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medimobile.ui.screens.dataentry.DataEntryScreen
import com.example.medimobile.ui.screens.encounterupdate.UpdateEncounterScreen
import com.example.medimobile.ui.screens.menus.LoginScreen
import com.example.medimobile.ui.screens.menus.MainMenuScreen
import com.example.medimobile.ui.screens.settings.SettingsScreen
import com.example.medimobile.ui.theme.MediMobileTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediMobileTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") { LoginScreen(navController) }
                    composable("mainMenu") { MainMenuScreen(navController) }
                    composable("dataEntry") { DataEntryScreen(navController) }
                    composable("settings") { SettingsScreen(navController) }
                    composable("updateEncounter") { UpdateEncounterScreen(navController) }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    MediMobileTheme {
        UpdateEncounterScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainMenuPreview() {
    MediMobileTheme {
        MainMenuScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsPreview() {
    MediMobileTheme {
        SettingsScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DataEntryPreview() {
    MediMobileTheme {
        DataEntryScreen(navController = rememberNavController())
    }
}
