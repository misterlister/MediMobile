package com.example.medimobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun LoadingIndicator(isLoading: Boolean, modifier: Modifier = Modifier) {
    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                // Set the background color to semi-transparent black
                .background(Color(0x80000000))
                .pointerInput(Unit) {
                    // Block user interaction while loading
                    detectTapGestures()
                }
        ) {
            // Align the progress indicator in the center of the Box
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}