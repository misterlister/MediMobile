package com.example.medimobile.ui.components.inputfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.constants.DropdownConstants
import com.example.medimobile.ui.components.templates.RadioButtonWithText
import com.example.medimobile.ui.util.highlightIf

@Composable
fun TriageRadioButtons(
    selectedOption: String?,
    emptyHighlight: Boolean = false,
    modifier: Modifier = Modifier,
    onOptionSelected: (String?) -> Unit
) {
    val options = DropdownConstants.TRIAGE_LEVELS
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .highlightIf(emptyHighlight && (selectedOption !in options))
            .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 16.dp)
    ) {
        options.forEach { option ->
            RadioButtonWithText(
                option = option,
                selectedOption = selectedOption,
                modifier = modifier,
                onOptionSelected = onOptionSelected
            )
        }
    }
}