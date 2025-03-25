package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun ScreenLayout(
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    focusManager: FocusManager = LocalFocusManager.current,
    ) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding()
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Clears focus when tapping outside of fields
                        focusManager.clearFocus()
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // **Top Bar Section**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                topBar()
            }

            // **Main Content Section**
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                content()
            }

            // **Bottom Button Bar Section**
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomBar()
            }
        }
    }
}
