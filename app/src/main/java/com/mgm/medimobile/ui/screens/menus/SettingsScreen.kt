package com.mgm.medimobile.ui.screens.menus


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mgm.medimobile.R
import com.mgm.medimobile.data.session.UserSessionManager
import com.mgm.medimobile.data.utils.DateRangeOption
import com.mgm.medimobile.data.utils.toDateRangeOption
import com.mgm.medimobile.data.utils.toDisplayValue
import com.mgm.medimobile.data.utils.toText
import com.mgm.medimobile.ui.components.ScreenTitle
import com.mgm.medimobile.ui.components.UsernameText
import com.mgm.medimobile.ui.components.dropdowns.BaseDropdown
import com.mgm.medimobile.ui.components.templates.DividedFormSections
import com.mgm.medimobile.ui.components.templates.FormSectionData
import com.mgm.medimobile.ui.components.templates.MediButton
import com.mgm.medimobile.ui.components.templates.RadioButtonWithText
import com.mgm.medimobile.ui.components.templates.ScreenLayout
import com.mgm.medimobile.ui.theme.BrightnessMode
import com.mgm.medimobile.ui.theme.ContrastLevel
import com.mgm.medimobile.viewmodel.MediMobileViewModel


@Composable
fun SettingsScreen(navController: NavController, viewModel: MediMobileViewModel) {

    val encounterDateRange = viewModel.selectedDateRange.collectAsState()
    val dateRangeOptions = DateRangeOption.entries.map { it.toDisplayValue() }
    val username = UserSessionManager.userDisplayName

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
                    text = stringResource(R.string.settings_title),
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
                    FormSectionData("Brightness Mode") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 16.dp)
                        ) {
                            BrightnessMode.entries.forEach { mode ->
                                RadioButtonWithText(
                                    option = mode.toText(),
                                    selectedOption = viewModel.brightnessMode.value.toText(),
                                    onOptionSelected = { viewModel.setBrightnessMode(mode) }
                                )
                            }
                        }
                    },
                    FormSectionData("Contrast Level") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 16.dp)
                        ) {
                            ContrastLevel.entries.forEach { level ->
                                RadioButtonWithText(
                                    option = level.toText(),
                                    selectedOption = viewModel.contrastLevel.value.toText(),
                                    onOptionSelected = { viewModel.setContrastLevel(level) }
                                )
                            }
                        }
                    }
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