package com.example.medimobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.theme.MediMobileTheme
import com.example.medimobile.ui.theme.appTitleTextStyle
import com.example.medimobile.ui.theme.sectionTitleTextStyle
import com.example.medimobile.ui.theme.userNameTextStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    val tabs = listOf("Triage", "Information Collection", "Discharge")
    val selectedTabIndex = remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MediMobile",
                style = appTitleTextStyle
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Login Form
            TextField(value = "", onValueChange = {}, placeholder = { Text("username") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = "", onValueChange = {}, placeholder = { Text("password") })
            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Login")
            }
        }
    }
}

@Composable
fun DataEntryScreen(modifier: Modifier = Modifier) {
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
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Cancel", color = Color.Black)
                }
                Button(onClick = { /*TODO*/ }) {
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

// Radio buttons for Triage
@Composable
fun TriageRadioButtons(selectedOption: String?, onOptionSelected: (String) -> Unit) {
    val options = listOf("Green", "Yellow", "Red", "White")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) }
                )
                Text(text = option)
            }
        }
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
            TriageRadioButtons(selectedOption = selectedOption.value, onOptionSelected = { selectedOption.value = it })
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
                modifier = Modifier.fillMaxWidth(0.3f)
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
                    // DropdownMenu that uses the expanded state to show/hide
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MediMobileTheme {
        LoginScreen()
    }
}