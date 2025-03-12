package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

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
