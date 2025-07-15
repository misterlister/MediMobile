package com.mgm.medimobile.ui.components.templates

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


@Composable
fun ScreenLayout(
    topBar: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
    bottomBar: @Composable (() -> Unit)? = null,
    focusManager: FocusManager = LocalFocusManager.current,
    ) {

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            // **Top Bar Section (Optional)**
            if (topBar != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.inversePrimary)
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    topBar()
                }
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

            // **Bottom Button Bar Section (Optional)**
            if (bottomBar != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.inversePrimary)
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bottomBar()
                }
            }
        }
    }
}
