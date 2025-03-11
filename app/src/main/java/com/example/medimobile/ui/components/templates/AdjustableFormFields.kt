package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.medimobile.ui.components.HorizontalSectionDivider
import com.example.medimobile.ui.components.VerticalSectionDivider

@Composable
fun AdjustableFormFields(isLandscape: Boolean = false, formSections: List<FormSectionData>) {
    // Decide layout based on orientation
    if (isLandscape) {
        // Landscape: Use Row arrangement
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Render each form section with vertical dividers between them
            formSections.forEachIndexed { index, section ->
                FormSection(
                    title = section.title,
                    content = section.content,
                    isLandscape = true,
                    modifier = Modifier.weight(1f)
                )

                // Add a vertical divider between sections, but not after the last one
                if (index < formSections.size - 1) {
                    VerticalSectionDivider() // Vertical divider for landscape
                }
            }
        }
    } else {
        // Portrait: Use Column arrangement
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Render each form section with horizontal dividers between them
            formSections.forEachIndexed { index, section ->
                FormSection(
                    title = section.title,
                    content = section.content,
                    isLandscape = false
                )

                // Add a horizontal divider between sections, but not after the last one
                if (index < formSections.size - 1) {
                    HorizontalSectionDivider() // Horizontal divider for portrait
                }
            }
        }
    }
}