package com.example.medimobile.ui.screens.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.ui.theme.appTitleTextStyle

@Composable
fun LoginScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "MediMobile",
                style = appTitleTextStyle,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Login Form
            TextField(value = "", onValueChange = {}, placeholder = { Text("username") })
            Spacer(modifier = Modifier.weight(0.125f))
            TextField(value = "", onValueChange = {}, placeholder = { Text("password") })

            Spacer(modifier = Modifier.weight(0.5f))

            // Login Button
            Button(onClick = { navController.navigate("mainMenu")  }) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}