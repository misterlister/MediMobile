package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.medimobile.ui.components.HorizontalSectionDivider

@Composable
fun DividedFormSections(formSections: List<FormSectionData>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Render each form section with horizontal dividers between them
        formSections.forEachIndexed { index, section ->
            FormSection(
                title = section.title, // Title for the section
                content = section.content, // Content to be displayed in the section
            )

            // Add a horizontal divider between sections, but not after the last one
            if (index < formSections.size - 1) {
                HorizontalSectionDivider()
            }
        }
    }
}