package com.mgm.medimobile.ui.components.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonWithText(
    option: String,
    selectedOption: String?,
    modifier: Modifier = Modifier,
    onOptionSelected: (String?) -> Unit
) {
    val isSelected = selectedOption?.equals(option, ignoreCase = true) == true

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                if (isSelected) {
                    onOptionSelected(null) // Deselect if clicked again
                } else {
                    onOptionSelected(option)
                }
            },
            modifier = modifier.testTag("${option}RadioButton")
        )
        Text(text = option)
    }
}
