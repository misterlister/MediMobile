package com.example.medimobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medimobile.ui.theme.MediMobileTheme
import com.example.medimobile.ui.theme.appTitleTextStyle
import com.example.medimobile.ui.theme.sectionTitleTextStyle
import com.example.medimobile.ui.theme.userNameTextStyle
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed


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

@Composable
fun LoginScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "MediMobile",
                style = appTitleTextStyle,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Login Form
            TextField(value = "", onValueChange = {}, placeholder = { Text("username") })
            Spacer(modifier = Modifier.weight(0.125f))
            TextField(value = "", onValueChange = {}, placeholder = { Text("password") })

            Spacer(modifier = Modifier.weight(0.5f))

            // Login Button
            Button(onClick = { navController.navigate("mainMenu")  }) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun MainMenuScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

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
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "User Name",
                    style = userNameTextStyle)
                Button(onClick = { navController.navigate("login")  }) {
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
                Button(onClick = { navController.navigate("dataEntry") }) {
                    Text(text = "Create New Encounter")
                }

                Spacer(modifier = Modifier.weight(0.25f))

                Button(onClick = { navController.navigate("updateEncounter") }) {
                    Text(text = "Update Existing Encounter")
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Settings")
        }
    }
}

@Composable
fun DataEntryScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    val tabs = listOf("Triage", "Information Collection", "Discharge")
    val selectedTabIndex = remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // **Username, DocID, and Tabs Section**
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .statusBarsPadding()
            ) {
                // Username and DocID
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "User Name",
                        style = userNameTextStyle)
                    Text(
                        text = "Doc ID",
                        style = userNameTextStyle)
                }

                // Tab selection
                TabRow(
                    selectedTabIndex = selectedTabIndex.intValue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex.intValue == index,
                            onClick = { selectedTabIndex.intValue = index },
                            text = { Text(text = title) }
                        )
                    }
                }
            }

            // **Main Content Section**
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTabIndex.intValue) {
                    0 -> TriageScreen(isLandscape = isLandscape)
                    1 -> InformationCollectionScreen(isLandscape = isLandscape)
                    2 -> DischargeScreen(isLandscape = isLandscape)
                }
            }

            // **Button Section**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
                    .navigationBarsPadding()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { navController.navigate("mainMenu")  }) {
                    Text(text = "Cancel", color = Color.Black)
                }
                Button(onClick = { navController.navigate("mainMenu")  }) {
                    Text(text = "Save", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun HorizontalSectionDivider() {
    HorizontalDivider(thickness = 1.dp, color = Color.Black)
}

@Composable
fun VerticalSectionDivider() {
    VerticalDivider(thickness = 1.dp, color = Color.Black)
}

@Composable
fun FormSection(
    title: String,
    content: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false,
) {
    val spacing: Dp = if (isLandscape) 16.dp else 8.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title, style = sectionTitleTextStyle)
        Spacer(modifier = Modifier.height(spacing))
        content(modifier)
        Spacer(modifier = Modifier.height(spacing))
    }
}

// Data class to hold the title and content for each form section
data class FormSectionData(
    val title: String,
    val content: @Composable (modifier: Modifier) -> Unit
)

@Composable
fun TriageRadioButtons(isLandscape: Boolean = false, selectedOption: String?, onOptionSelected: (String) -> Unit) {
    val options = listOf("Green", "Yellow", "Red", "White")

    // Arrange in grid format for landscape orientation
    if (isLandscape) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    RadioButtonWithText(option = options[0], selectedOption, onOptionSelected)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    RadioButtonWithText(option = options[1], selectedOption, onOptionSelected)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    RadioButtonWithText(option = options[2], selectedOption, onOptionSelected)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                    RadioButtonWithText(option = options[3], selectedOption, onOptionSelected)
                }
            }
        }

    // Arrange in row format for portrait orientation
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEach { option ->
                RadioButtonWithText(option, selectedOption, onOptionSelected)
            }
        }
    }
}

@Composable
fun RadioButtonWithText(option: String, selectedOption: String?, onOptionSelected: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedOption == option,
            onClick = { onOptionSelected(option) }
        )
        Text(text = option)
    }
}

@Composable
fun DataEntryForm(modifier: Modifier = Modifier, isLandscape: Boolean = false, formSections: List<FormSectionData>) {
    // Decide layout based on orientation
    if (isLandscape) {
        // Landscape: Use Row arrangement
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Render each form section with vertical dividers between them
            formSections.forEachIndexed { index, section ->
                FormSection(
                    title = section.title,
                    content = section.content,
                    isLandscape = isLandscape,
                    modifier = Modifier.weight(1f)
                )

                // Add a vertical divider between sections, but not after the last one
                if (index < formSections.size - 1) {
                    VerticalSectionDivider() // Vertical divider for landscape
                }
            }
        }
    } else {
        // Portrait: Use Column arrangement
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Render each form section with horizontal dividers between them
            formSections.forEachIndexed { index, section ->
                FormSection(
                    title = section.title,
                    content = section.content,
                    isLandscape = isLandscape
                )

                // Add a horizontal divider between sections, but not after the last one
                if (index < formSections.size - 1) {
                    HorizontalSectionDivider() // Horizontal divider for portrait
                }
            }
        }
    }
}

// Screen for Triage Tab
@Composable
fun TriageScreen(isLandscape: Boolean = false) {
    val expandedArrivalMethodState = remember { mutableStateOf(false) }
    val formSections = listOf(
        FormSectionData("Arrival Time") {
            TextField(value = "", onValueChange = {}, placeholder = { Text("Enter arrival time") })
        },
        FormSectionData("Triage") {
            val selectedOption = remember { mutableStateOf<String?>(null) }
            TriageRadioButtons(isLandscape, selectedOption = selectedOption.value, onOptionSelected = { selectedOption.value = it })
        },
        FormSectionData("Visit ID") {
            TextField(value = "", onValueChange = {}, placeholder = { Text("Enter Visit ID") })
        },
        FormSectionData("Arrival Method") {
            Box(modifier = Modifier.wrapContentSize()) {
                Column {
                    // Button to toggle the DropdownMenu expansion
                    Button(onClick = { expandedArrivalMethodState.value = !expandedArrivalMethodState.value }) {
                        Text(text = "Select Arrival Method")
                    }
                    // DropdownMenu that uses the expanded state to show/hide
                    DropdownMenu(
                        expanded = expandedArrivalMethodState.value,
                        onDismissRequest = { expandedArrivalMethodState.value = false }
                    ) {
                        DropdownMenuItem(text = { Text("Ambulance") }, onClick = { expandedArrivalMethodState.value = false })
                        DropdownMenuItem(text = { Text("Walk-in") }, onClick = { expandedArrivalMethodState.value = false })
                    }
                }
            }
        }
    )
    DataEntryForm(formSections = formSections, isLandscape = isLandscape)
}

// Screen for Information Collection Tab
@Composable
fun InformationCollectionScreen(isLandscape: Boolean = false) {
    val expandedRoleState = remember { mutableStateOf(false) }
    val expandedChiefComplaintState = remember { mutableStateOf(false) }
    val formSections = listOf(
        FormSectionData("Age") {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter age") },
                modifier = Modifier.fillMaxWidth(0.4f)
            )
        },
        FormSectionData("Role") {
            Box(modifier = Modifier.wrapContentSize()) {
                Column {
                    // Button to toggle the DropdownMenu expansion
                    Button(onClick = { expandedRoleState.value = !expandedRoleState.value }) {
                        Text(text = "Select Arrival Method")
                    }
                    // DropdownMenu that uses the expanded state to show/hide
                    DropdownMenu(
                        expanded = expandedRoleState.value,
                        onDismissRequest = { expandedRoleState.value = false }
                    ) {
                        DropdownMenuItem(text = { Text("Staff") }, onClick = { expandedRoleState.value = false })
                        DropdownMenuItem(text = { Text("Attendee") }, onClick = { expandedRoleState.value = false })
                    }
                }
            }
        },
        FormSectionData("Chief Complaint") {
            Box(modifier = Modifier.wrapContentSize()) {
                Column {
                    // Button to toggle the DropdownMenu expansion
                    Button(onClick = { expandedChiefComplaintState.value = !expandedChiefComplaintState.value }) {
                        Text(text = "Select Chief Complaint")
                    }
                    // DropdownMenu that uses the expanded state to show/hide
                    DropdownMenu(
                        expanded = expandedChiefComplaintState.value,
                        onDismissRequest = { expandedChiefComplaintState.value = false }
                    ) {
                        DropdownMenuItem(text = { Text("Nausea") }, onClick = { expandedChiefComplaintState.value = false })
                        DropdownMenuItem(text = { Text("Dizziness") }, onClick = { expandedChiefComplaintState.value = false })
                    }
                }
            }
        },
        FormSectionData("Comments") {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter comments (optional)") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(150.dp)
            )
        },
    )
    DataEntryForm(formSections = formSections, isLandscape = isLandscape)
}

// Screen for Discharge Tab
@Composable
fun DischargeScreen(isLandscape: Boolean = false) {
    val expandedDestinationState = remember { mutableStateOf(false) }
    val formSections = listOf(
        FormSectionData("Departure Time") {
            TextField(value = "", onValueChange = {}, placeholder = { Text("Enter age") })
        },
        FormSectionData("Departure Destination") {
            Box(modifier = Modifier.wrapContentSize()) {
                Column {
                    // Button to toggle the DropdownMenu expansion
                    Button(onClick = { expandedDestinationState.value = !expandedDestinationState.value }) {
                        Text(text = "Select Arrival Method")
                    }
                    DropdownMenu(
                        expanded = expandedDestinationState.value,
                        onDismissRequest = { expandedDestinationState.value = false }
                    ) {
                        DropdownMenuItem(text = { Text("Back to Event") }, onClick = { expandedDestinationState.value = false })
                        DropdownMenuItem(text = { Text("Hospital via ambulance") }, onClick = { expandedDestinationState.value = false })
                    }
                }
            }
        },
        FormSectionData("Discharge Diagnosis") {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Enter Discharge Diagnosis (optional)") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(150.dp)
            )
        },
    )
    DataEntryForm(formSections = formSections, isLandscape = isLandscape)
}

enum class StageStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETE
}

data class Encounter(
    val encounterID: String,
    val docID: String,
    val visitID: String,
    val arrivalTime: String,
    val triageState: String,
    val arrivalMethod: String,
    val age: Int,
    val chiefComplaint: String,
    val comments: String,
    val departureTime: String,
    val departureDestination: String,
    val dischargeDiagnosis: String,
    val triageStatus: StageStatus,
    val informationCollectionStatus: StageStatus,
    val dischargeStatus: StageStatus,
    val complete: Boolean
)

// Header Cell Style
@Composable
fun TableHeaderCell(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

// Data Cell Style
@Composable
fun TableCell(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center
    )
}

@Composable
fun EncounterTable(
    records: List<Encounter>,
    onRowClick: (Encounter) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
            .border(2.dp, Color.Black)
    ) {
        // Table Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .border(1.dp, Color.Black)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TableHeaderCell("DocID")
                TableHeaderCell("VisitID")
                TableHeaderCell("Date")
                TableHeaderCell("Status")
            }
        }

        // Table Data Rows
        itemsIndexed(records) { index, record ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRowClick(record) }
                    .background(if (index % 2 == 0) Color.LightGray else Color.White)
                    .border(1.dp, Color.Black)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TableCell(record.docID)
                TableCell(record.visitID)
                TableCell(record.arrivalTime)
                TableCell(if (record.complete) "Complete" else "Incomplete")
            }
        }
    }
}

// Screen for updating Encounters
@Composable
fun UpdateEncounterScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
            ) {
                Text(text = "User Name", style = userNameTextStyle)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(0.25f))

                Text(text = "Update Encounter", style = appTitleTextStyle)

                Spacer(modifier = Modifier.weight(0.25f))

                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Refresh")
                }

                Spacer(modifier = Modifier.weight(0.25f))

                Box(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxWidth()
                ) {
                    EncounterTable(
                        records = getDummyEncounters(),
                        onRowClick = { selectedRecord ->
                            println("Selected Encounter: ${selectedRecord.encounterID}")
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(0.25f))

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "QR Scan")
                    }
                    TextField(value = "", onValueChange = {}, placeholder = { Text("Doc ID") })
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.LightGray)
                .padding(16.dp)
                .navigationBarsPadding()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { navController.navigate("mainMenu") },
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                Text(text = "Back", color = Color.Black)
            }
        }
    }
}

// Screen for adjusting settings
@Composable
fun SettingsScreen(navController: NavController) {
    val expandedEventState = remember { mutableStateOf(false) }
    val expandedLocationState = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
            ) {
                Text(text = "User Name", style = userNameTextStyle)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                Text(text = "Settings", style = appTitleTextStyle)

                Spacer(modifier = Modifier.weight(0.5f))

                val formSections = listOf(
                    FormSectionData("Event") {
                        Box(modifier = Modifier.wrapContentSize()) {
                            Column {
                                Button(onClick = { expandedEventState.value = !expandedEventState.value }) {
                                    Text(text = "Select Event")
                                }
                                DropdownMenu(
                                    expanded = expandedEventState.value,
                                    onDismissRequest = { expandedEventState.value = false }
                                ) {
                                    DropdownMenuItem(text = { Text("Shambhala") }, onClick = { expandedEventState.value = false })
                                }
                            }
                        }
                    },
                    FormSectionData("Location") {
                        Box(modifier = Modifier.wrapContentSize()) {
                            Column {
                                Button(onClick = { expandedLocationState.value = !expandedLocationState.value }) {
                                    Text(text = "Select Location")
                                }
                                DropdownMenu(
                                    expanded = expandedLocationState.value,
                                    onDismissRequest = { expandedLocationState.value = false }
                                ) {
                                    DropdownMenuItem(text = { Text("Main Medical") }, onClick = { expandedLocationState.value = false })
                                }
                            }
                        }
                    },
                )
                DataEntryForm(formSections = formSections, isLandscape = isLandscape)

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Back button at the bottom center
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Ensure it's at the bottom
                .background(Color.LightGray)
                .padding(16.dp)
                .navigationBarsPadding()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { navController.navigate("mainMenu") },
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                Text(text = "Back", color = Color.Black)
            }
        }
    }
}


fun getDummyEncounters(): List<Encounter> {
    return listOf(
        Encounter(
            encounterID = "E001",
            docID = "D001",
            visitID = "V100",
            arrivalTime = "2025-02-19",
            triageState = "Green",
            arrivalMethod = "Ambulance",
            age = 30,
            chiefComplaint = "Headache",
            comments = "No allergies",
            departureTime = "2025-02-19 14:30",
            departureDestination = "Home",
            dischargeDiagnosis = "Migraine",
            triageStatus = StageStatus.IN_PROGRESS,
            informationCollectionStatus = StageStatus.NOT_STARTED,
            dischargeStatus = StageStatus.COMPLETE,
            complete = false
        ),
        Encounter(
            encounterID = "E002",
            docID = "D002",
            visitID = "V101",
            arrivalTime = "2025-02-18",
            triageState = "Yellow",
            arrivalMethod = "Walk-in",
            age = 45,
            chiefComplaint = "Chest Pain",
            comments = "Family history of heart disease",
            departureTime = "2025-02-18 16:00",
            departureDestination = "ICU",
            dischargeDiagnosis = "Angina",
            triageStatus = StageStatus.COMPLETE,
            informationCollectionStatus = StageStatus.IN_PROGRESS,
            dischargeStatus = StageStatus.NOT_STARTED,
            complete = true
        ),
        Encounter(
            encounterID = "E003",
            docID = "D003",
            visitID = "V102",
            arrivalTime = "2025-02-17",
            triageState = "Red",
            arrivalMethod = "Helicopter",
            age = 60,
            chiefComplaint = "Stroke symptoms",
            comments = "High blood pressure",
            departureTime = "2025-02-17 20:00",
            departureDestination = "Neurology",
            dischargeDiagnosis = "Stroke",
            triageStatus = StageStatus.NOT_STARTED,
            informationCollectionStatus = StageStatus.COMPLETE,
            dischargeStatus = StageStatus.IN_PROGRESS,
            complete = false
        )
    )
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
