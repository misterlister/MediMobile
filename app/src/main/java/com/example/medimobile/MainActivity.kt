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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "MediMobile",
            modifier = Modifier
                .fillMaxWidth(),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        )
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "User Name")
            Text(text = "Doc ID")
        }
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

        when (selectedTabIndex.intValue) {
            0 -> TriageScreen()
            1 -> InformationCollectionScreen()
            2 -> DischargeScreen()
        }
    }
}

@Composable
fun TriageScreen() {
    Text(text = "Triage Screen")
}

@Composable
fun InformationCollectionScreen() {
    Text(text = "Information Collection Screen")
}

@Composable
fun DischargeScreen() {
    Text(text = "Discharge Screen")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MediMobileTheme {
        DataEntryScreen()
    }
}