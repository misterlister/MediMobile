package com.example.medimobile.ui.components.inputfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.medimobile.data.utils.ShambhalaDropdowns
import com.example.medimobile.ui.components.templates.RadioButtonWithText

@Composable
fun TriageRadioButtons(selectedOption: String?, onOptionSelected: (String) -> Unit) {
    val options = ShambhalaDropdowns.triageLevels
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            RadioButtonWithText(option.displayValue, selectedOption, onOptionSelected)
        }
    }
}