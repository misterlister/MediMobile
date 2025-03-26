package com.example.medimobile.ui.screens.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medimobile.data.utils.toDisplayValues
import com.example.medimobile.ui.components.templates.BaseDropdown
import com.example.medimobile.ui.theme.appTitleTextStyle
import com.example.medimobile.ui.theme.errorMessageTextStyle
import com.example.medimobile.viewmodel.MediMobileViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: MediMobileViewModel) {

    // variables to hold user input for username and password
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // currently selected group event
    val group = viewModel.userGroup.collectAsState().value
    val selectedEvent = viewModel.selectedEvent.collectAsState().value

    var errorMessage by remember { mutableStateOf<String>("") }
    val loginResult by viewModel.loginResult.collectAsState()

    // controls which fields are selected
    val focusManager = LocalFocusManager.current

    // specific focus requesters for each field
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }


    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            result.onSuccess {
                viewModel.setCurrentUser(username)
                navController.navigate("mainMenu")
            }.onFailure { error ->
                errorMessage = "Login failed: ${error.message}"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .navigationBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Clears focus when tapping outside of fields
                        focusManager.clearFocus()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {

            Spacer(modifier = Modifier.weight(0.15f))

            Text(
                text = "MediMobile",
                style = appTitleTextStyle,
                textAlign = TextAlign.Center,
            )

            Text(
                text = errorMessage,
                style = errorMessageTextStyle,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
                )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
            )
            {
                BaseDropdown(
                    currentSelection = group ?: "",
                    options = selectedEvent?.userGroups?.toDisplayValues(),
                    dropdownLabel = "Sign-in to",
                    onSelectionChanged = { newLocation ->
                        viewModel.setUserGroup(newLocation)
                    },
                )
            }
            // Login Form
            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("username") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    },
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                modifier = Modifier
                    .focusRequester(usernameFocusRequester)
                    .fillMaxWidth(0.75f)
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        viewModel.login(username, password, group ?: "")
                    }
                ),
                singleLine = true,
                modifier = Modifier
                    .focusRequester(passwordFocusRequester)
                    .fillMaxWidth(0.75f)
            )

            // Login Button
            Button(onClick = { viewModel.login(username, password, group ?: "") }) {
                Text("Login")
            }
            Spacer(modifier = Modifier.weight(1f))

        }

        Button(
            onClick = { navController.navigate("eventSelect") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Text(text = "Event Select")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current), viewModel = MediMobileViewModel())
}