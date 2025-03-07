package com.example.medimobile.ui.screens.encounterupdate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.utils.formatArrivalDateTime

// Cell Headers for the Encounter Table
@Composable
fun TableHeaderCell(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier.padding(horizontal = 8.dp)
    )
}

// Cells for the Encounter Table
@Composable
fun TableCell(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(horizontal = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun EncounterTable(
    records: List<PatientEncounter>,
    onRowClick: (PatientEncounter) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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
                    .padding(vertical = 8.dp)
            ) {
                TableHeaderCell("DocID", modifier = Modifier.weight(1f))
                TableHeaderCell("VisitID", modifier = Modifier.weight(1f))
                TableHeaderCell("Date", modifier = Modifier.weight(1.5f))
                TableHeaderCell("Status", modifier = Modifier.weight(1.5f))
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
                    .padding(vertical = 8.dp)
            ) {
                TableCell(record.documentNum, modifier = Modifier.weight(1f))
                TableCell(record.visitId, modifier = Modifier.weight(1f))
                TableCell(formatArrivalDateTime(record), modifier = Modifier.weight(1.5f))
                TableCell(if (record.complete) "Complete" else "Incomplete", modifier = Modifier.weight(1.5f))
            }
        }
    }
}
