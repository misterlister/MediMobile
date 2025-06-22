package com.example.medimobile.ui.screens.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.data.eventdata.EventList
import com.example.medimobile.data.utils.toEventNames
import com.example.medimobile.ui.components.ScreenTitle
import com.example.medimobile.ui.components.dropdowns.BaseDropdown
import com.example.medimobile.ui.components.templates.DividedFormSections
import com.example.medimobile.ui.components.templates.FormSectionData
import com.example.medimobile.ui.components.templates.MediButton
import com.example.medimobile.ui.components.templates.ScreenLayout
import com.example.medimobile.viewmodel.MediMobileViewModel


@Composable
fun EventSelectScreen(navController: NavController, viewModel: MediMobileViewModel) {

    val selectedEvent = viewModel.selectedEvent.collectAsState().value
    val selectedLocation = viewModel.selectedLocation.collectAsState().value

    ScreenLayout(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                ScreenTitle(
                    text = "Event Select",
                    modifier = Modifier.padding(vertical = 32.dp))

                val formSections = listOf(
                    FormSectionData("Event") {
                        BaseDropdown(
                            currentSelection = selectedEvent?.eventName,
                            options = EventList.EVENTS.toEventNames(),
                            dropdownLabel = "Event",
                            onSelectionChanged = { newEvent ->
                                if (newEvent != null) {
                                    viewModel.setSelectedEvent(newEvent)
                                }
                            },
                            width = 0.8f,
                            notNullable = true,
                            modifier = Modifier.testTag("eventDropdown")
                        )
                    },

                    FormSectionData("Location") {
                        BaseDropdown(
                            currentSelection = selectedLocation?.locationName ?: "",
                            options = selectedEvent?.locations?.map { it.locationName },
                            dropdownLabel = "Location",
                            onSelectionChanged = { selectedLocation ->
                                val matchedLocation = selectedEvent?.locations?.find {
                                    it.locationName == selectedLocation
                                }
                                matchedLocation?.let {
                                    viewModel.setSelectedLocation(it)
                                }
                            },
                            width = 0.8f,
                            notNullable = true,
                            modifier = Modifier.testTag("locationDropdown")
                        )
                    },
                )
                DividedFormSections(formSections = formSections)
            }
        },
        bottomBar = {
            MediButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.testTag("eventBackButton")
            ) {
                Text(text = "Back")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventSelectScreenPreview() {
    EventSelectScreen(navController = NavController(LocalContext.current), viewModel = MediMobileViewModel())
}