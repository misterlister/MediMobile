package com.example.medimobile.ui.screens.menus


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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.data.utils.DateRangeOption
import com.example.medimobile.data.utils.toDateRangeOption
import com.example.medimobile.data.utils.toDisplayValue
import com.example.medimobile.ui.components.dropdowns.BaseDropdown
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.components.templates.MediButton
import com.example.medimobile.ui.theme.screenTitleTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel


@Composable
fun SettingsScreen(navController: NavController, viewModel: MediMobileViewModel) {

    val encounterDateRange = viewModel.selectedDateRange.collectAsState()
    val dateRangeOptions = DateRangeOption.entries.map { it.toDisplayValue() }

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(text = "Settings", style = screenTitleTextStyle)

                Spacer(modifier = Modifier.weight(0.5f))

                val formSections = listOf(
                    FormSectionData("Encounter Table Date Range") {
                        BaseDropdown(
                            currentSelection = encounterDateRange.value.toDisplayValue(),
                            options = dateRangeOptions,
                            dropdownLabel = "Date Range",
                            onSelectionChanged = { selectedDisplayValue ->
                                // Convert the selected display value back to DateRangeOption
                                val selectedOption = selectedDisplayValue.toDateRangeOption()
                                viewModel.setSelectedDateRange(selectedOption)
                            },
                            width = 0.8f
                        )
                    },
                )
                DividedFormSections(formSections = formSections)

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Back button at the bottom center
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.LightGray)
                .padding(12.dp)
                .navigationBarsPadding()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            MediButton(
                onClick = { navController.navigate("mainMenu") },
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                Text(text = "Back", color = Color.Black)
            }
        }
    }
}