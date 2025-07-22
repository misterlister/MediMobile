package com.mgm.medimobile.ui.components.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mgm.medimobile.ui.components.HorizontalSectionDivider

@Composable
fun DividedFormSections(formSections: List<FormSectionData>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Render each form section with horizontal dividers between them
            formSections.forEachIndexed { index, section ->
                FormSection(
                    title = section.title, // Title for the section
                    content = section.content, // Content to be displayed in the section
                    required = section.required // Whether the section should show a star next to its title
                )

                // Add a horizontal divider between sections, but not after the last one
                if (index < formSections.size - 1) {
                    HorizontalSectionDivider()
                }
            }
        }
    }
}