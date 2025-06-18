package com.example.medimobile.ui.screens.encounterupdate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import com.example.medimobile.data.constants.UIConstants.COMPLETE_ABBREVIATION
import com.example.medimobile.data.constants.UIConstants.INCOMPLETE_ABBREVIATION
import com.example.medimobile.data.constants.UIConstants.LOADING_MESSAGE
import com.example.medimobile.data.constants.UIConstants.NO_ENCOUNTERS_MESSAGE

// Cell Headers for the Encounter Table
@Composable
fun TableHeaderCell(text: String, modifier: Modifier = Modifier, onClick: () -> Unit, isSorted: Boolean, isDescending: Boolean) {
    Row(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
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
        modifier = modifier.padding(horizontal = 4.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun EncounterTable(
    records: List<PatientEncounter>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onRowClick: (PatientEncounter) -> Unit,

) {

    var sortColumn by remember { mutableStateOf(SortColumn.Date) }
    var isDescending by remember { mutableStateOf(true) }

    // Sort Encounters based on the selected column
    val sortedRecords = remember(sortColumn, isDescending, records) {
        when (sortColumn) {
            SortColumn.VisitId -> records.sortedWith(compareBy { it.visitId })
            SortColumn.Date -> records.sortedWith(
                compareBy<PatientEncounter> { it.arrivalDate }
                    .thenBy { it.arrivalTime }
            )
            SortColumn.Status -> records.sortedWith(compareBy { it.complete })
        }.let {
            if (isDescending) it.reversed() else it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(2.dp, MaterialTheme.colorScheme.outline)
            .shadow(2.dp)
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(1.dp, MaterialTheme.colorScheme.outline)
                .padding(vertical = 8.dp)
        ) {
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
                "Date (D/M/Y H:M)",
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
        // If there are no encounters, show a message
        if (records.isEmpty()) {
            val message = if (isLoading) LOADING_MESSAGE else NO_ENCOUNTERS_MESSAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("emptyEncounterTable"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Otherwise, show the content for the Encounter Table
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(sortedRecords) { index, record ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRowClick(record) }
                            .background(
                                if (index % 2 == 0)
                                    MaterialTheme.colorScheme.surfaceVariant
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                            .border(1.dp, MaterialTheme.colorScheme.outline)
                            .padding(vertical = 8.dp)
                            .testTag("encounterTableRow")
                    ) {
                        TableCell(record.visitId, modifier = Modifier.weight(VISIT_ID_WEIGHT))
                        TableCell(formatArrivalDateTime(record), modifier = Modifier.weight(DATE_WEIGHT))
                        TableCell(
                            text = if (record.complete) COMPLETE_ABBREVIATION else INCOMPLETE_ABBREVIATION,
                            modifier = Modifier.weight(STATUS_WEIGHT))
                    }
                }
            }
        }
    }
}

enum class SortColumn {
    VisitId, Date, Status
}

const val VISIT_ID_WEIGHT = 1.1f
const val DATE_WEIGHT = 1f
const val STATUS_WEIGHT = 0.5f