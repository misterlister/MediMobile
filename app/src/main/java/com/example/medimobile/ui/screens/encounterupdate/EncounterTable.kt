package com.example.medimobile.ui.screens.encounterupdate

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.model.PatientEncounter
import com.example.medimobile.data.utils.formatArrivalDateTime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.graphicsLayer

// Cell Headers for the Encounter Table
@Composable
fun TableHeaderCell(text: String, modifier: Modifier = Modifier, onClick: () -> Unit, isSorted: Boolean, isDescending: Boolean) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        // Show an arrow icon when the column is sorted
        if (isSorted) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = if (isDescending) "Sorted Descending" else "Sorted Ascending",
                modifier = Modifier
                    .size(16.dp)
                    .graphicsLayer { rotationX = if (isDescending) 180f else 0f },
                tint = Color.White
            )
        }
    }
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

// Suppress warnings about experimental use of stickyHeader
// If it is deprecated or supported in the future, this can be adjusted
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EncounterTable(
    records: List<PatientEncounter>,
    onRowClick: (PatientEncounter) -> Unit,
) {
    var sortColumn by remember { mutableStateOf(SortColumn.Date) }
    var isDescending by remember { mutableStateOf(false) }

    // Sort Encounters based on the selected column
    val sortedRecords = remember(sortColumn, isDescending, records) {
        when (sortColumn) {
            SortColumn.DocId -> records.sortedWith(compareBy { it.documentNum })
            SortColumn.VisitId -> records.sortedWith(compareBy { it.visitId })
            SortColumn.Date -> records.sortedWith(compareBy { it.arrivalDate })
            SortColumn.Status -> records.sortedWith(compareBy { it.complete })
        }.let {
            if (isDescending) it.reversed() else it
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .border(2.dp, Color.Black)
    ) {
        // Table Header
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .border(1.dp, Color.Black)
                    .padding(vertical = 8.dp)
            ) {
                TableHeaderCell(
                    "DocID",
                    modifier = Modifier.weight(DOC_WEIGHT),
                    onClick = {
                        sortColumn = SortColumn.DocId
                        isDescending = !isDescending
                    },
                    isSorted = sortColumn == SortColumn.DocId,
                    isDescending = isDescending
                )
                TableHeaderCell(
                    "VisitID",
                    modifier = Modifier.weight(VISIT_ID_WEIGHT),
                    onClick = {
                        sortColumn = SortColumn.VisitId
                        isDescending = !isDescending
                    },
                    isSorted = sortColumn == SortColumn.VisitId,
                    isDescending = isDescending
                )
                TableHeaderCell(
                    "Date",
                    modifier = Modifier.weight(DATE_WEIGHT),
                    onClick = {
                        sortColumn = SortColumn.Date
                        isDescending = !isDescending
                    },
                    isSorted = sortColumn == SortColumn.Date,
                    isDescending = isDescending
                )
                TableHeaderCell(
                    "Status",
                    modifier = Modifier.weight(STATUS_WEIGHT),
                    onClick = {
                        sortColumn = SortColumn.Status
                        isDescending = !isDescending
                    },
                    isSorted = sortColumn == SortColumn.Status,
                    isDescending = isDescending
                )
            }
        }

        // Table Data Rows
        itemsIndexed(sortedRecords) { index, record ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRowClick(record) }
                    .background(if (index % 2 == 0) Color.LightGray else Color.White)
                    .border(1.dp, Color.Black)
                    .padding(vertical = 8.dp)
            ) {
                TableCell(record.documentNum, modifier = Modifier.weight(DOC_WEIGHT))
                TableCell(record.visitId, modifier = Modifier.weight(VISIT_ID_WEIGHT))
                TableCell(formatArrivalDateTime(record), modifier = Modifier.weight(DATE_WEIGHT))
                TableCell(if (record.complete) "Comp." else "Incomp.", modifier = Modifier.weight(
                    STATUS_WEIGHT))
            }
        }
    }
}

enum class SortColumn {
    DocId, VisitId, Date, Status
}

const val DOC_WEIGHT = 1f
const val VISIT_ID_WEIGHT = 1.5f
const val DATE_WEIGHT = 1.2f
const val STATUS_WEIGHT = 0.9f