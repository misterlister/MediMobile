package com.example.medimobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.medimobile.ui.navigation.AppNavGraph
import com.example.medimobile.ui.screens.dataentry.DataEntryScreen
import com.example.medimobile.ui.screens.encounterupdate.UpdateEncounterScreen
import com.example.medimobile.ui.screens.menus.MainMenuScreen
import com.example.medimobile.ui.theme.MediMobileTheme
import com.example.medimobile.viewmodel.MediMobileViewModel


class MainActivity : ComponentActivity() {
    private val viewModel: MediMobileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediMobileTheme(
                brightnessMode = viewModel.brightnessMode.value,
                contrastLevel = viewModel.contrastLevel.value
            ) {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    MediMobileTheme {
        UpdateEncounterScreen(navController = rememberNavController(), MediMobileViewModel())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainMenuPreview() {
    MediMobileTheme {
        MainMenuScreen(navController = rememberNavController(), MediMobileViewModel())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DataEntryPreview() {
    MediMobileTheme {
        DataEntryScreen(navController = rememberNavController(), MediMobileViewModel())
    }
}
