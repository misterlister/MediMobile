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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.medimobile.ui.theme.MediMobileTheme
import com.example.medimobile.ui.theme.titleTextStyle
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DataEntryScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DataEntryScreen(modifier: Modifier = Modifier) {
    val tabs = listOf("Triage", "Information Collection", "Discharge")
    val selectedTabIndex = remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // **Title and Tabs Section (Fixed Height Using Weight)**
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f) // Take up 20% of the screen
                    .statusBarsPadding()
            ) {
                // Title header
                Text(
                    text = "MediMobile",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                )

                // User Name and Doc ID
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "User Name")
                    Text(text = "Doc ID")
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

            // **Main Content Section (Expands in Available Space)**
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f) // Content gets 65% of the screen
            ) {
                when (selectedTabIndex.intValue) {
                    0 -> TriageScreen()
                    1 -> InformationCollectionScreen()
                    2 -> DischargeScreen()
                }
            }

            // **Button Section (Fixed Height Using Weight)**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f) // Takes up 15% of the screen
                    .background(Color.LightGray)
                    .padding(16.dp)
                    .navigationBarsPadding(),
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
fun TriageScreen(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Text(text = "Arrival Time",
            style = titleTextStyle)
        Text(text = "Triage",
            style = titleTextStyle)
        Text(text = "Visit ID",
            style = titleTextStyle)
        Text(text = "Arrival Method",
            style = titleTextStyle)
    }
}

@Composable
fun InformationCollectionScreen(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Text(text = "Age",
            style = titleTextStyle)
        Text(text = "Role",
            style = titleTextStyle)
        Text(text = "Chief Complaint",
            style = titleTextStyle)
        Text(text = "Comments",
            style = titleTextStyle)
    }
}

@Composable
fun DischargeScreen(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Text(text = "Departure Time",
            style = titleTextStyle)
        Text(text = "Departure Destination",
            style = titleTextStyle)
        Text(text = "Discharge Diagnosis",
            style = titleTextStyle)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MediMobileTheme {
        DataEntryScreen()
    }
}