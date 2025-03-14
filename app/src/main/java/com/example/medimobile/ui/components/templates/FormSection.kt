package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.medimobile.ui.theme.sectionTitleTextStyle

@Composable
fun FormSection(
    title: String?,
    content: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing: Dp = 12.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (title != null) {
            Spacer(modifier = Modifier.height(spacing))
            Text(text = title, style = sectionTitleTextStyle)
        }
        Spacer(modifier = Modifier.height(spacing))
        content(modifier)
        Spacer(modifier = Modifier.height(spacing))
    }
}

// Data class to hold the title and content for each form section
data class FormSectionData(
    val title: String?,
    val content: @Composable (modifier: Modifier) -> Unit
)