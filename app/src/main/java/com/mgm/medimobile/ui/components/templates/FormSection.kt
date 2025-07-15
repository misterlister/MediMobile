package com.mgm.medimobile.ui.components.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mgm.medimobile.ui.components.SectionTitle

@Composable
fun FormSection(
    title: String?,
    content: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean = false
) {
    val spacing: Dp = 8.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (title != null) {
            Spacer(modifier = Modifier.height(spacing))
            Row {
                SectionTitle(text = title)
                if (required) {
                    Text(
                        text = " *",
                        color = Color.Red,
                        style = typography.titleMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(spacing))
        content(modifier)
        Spacer(modifier = Modifier.height(spacing))
    }
}

// Data class to hold the title and content for each form section
data class FormSectionData(
    val title: String?,
    val required: Boolean = false,
    val content: @Composable (modifier: Modifier) -> Unit
)