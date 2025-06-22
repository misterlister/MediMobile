package com.example.medimobile.ui.screens.menus


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.data.utils.DateRangeOption
import com.example.medimobile.data.utils.toDateRangeOption
import com.example.medimobile.data.utils.toDisplayValue
import com.example.medimobile.ui.components.ScreenTitle
import com.example.medimobile.ui.components.UsernameText
import com.example.medimobile.ui.components.dropdowns.BaseDropdown
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.components.templates.MediButton
import com.example.medimobile.ui.components.templates.ScreenLayout
import com.example.medimobile.viewmodel.MediMobileViewModel


@Composable
fun SettingsScreen(navController: NavController, viewModel: MediMobileViewModel) {

    val encounterDateRange = viewModel.selectedDateRange.collectAsState()
    val dateRangeOptions = DateRangeOption.entries.map { it.toDisplayValue() }
    val username = viewModel.currentUser.collectAsState().value

    ScreenLayout(
        topBar = {
            UsernameText(text = username)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                ScreenTitle(
                    text = "Settings",
                    modifier = Modifier.padding(vertical = 32.dp)
                )

                val formSections = listOf(
                    FormSectionData("Encounter Table Date Range") {
                        BaseDropdown(
                            currentSelection = encounterDateRange.value.toDisplayValue(),
                            options = dateRangeOptions,
                            dropdownLabel = "Date Range",
                            onSelectionChanged = { selectedDisplayValue ->
                                // Convert the selected display value back to DateRangeOption
                                val selectedOption = selectedDisplayValue?.toDateRangeOption()
                                if (selectedOption != null) {
                                    viewModel.setSelectedDateRange(selectedOption)
                                }
                            },
                            width = 0.8f,
                            notNullable = true
                        )
                    },

                    FormSectionData("Low Connectivity Mode") {
                        Switch(
                            checked = viewModel.lowConnectivityMode.value,
                            onCheckedChange = { isChecked ->
                                viewModel.setLowConnectivityMode(isChecked)
                            }
                        )
                    },
                )
                DividedFormSections(formSections = formSections)
            }
        },
        bottomBar = {
            MediButton(
                onClick = { navController.navigate("mainMenu") },
            ) {
                Text(text = "Back")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = NavController(LocalContext.current), viewModel = MediMobileViewModel())
}