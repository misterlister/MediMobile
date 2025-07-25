package com.mgm.medimobile.ui.screens.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.mgm.medimobile.data.utils.toDisplayValues
import com.mgm.medimobile.ui.components.AppTitle
import com.mgm.medimobile.ui.components.LoadingIndicator
import com.mgm.medimobile.ui.components.dropdowns.BaseDropdown
import com.mgm.medimobile.ui.components.templates.ErrorPopup
import com.mgm.medimobile.ui.components.templates.MediButton
import com.mgm.medimobile.ui.components.templates.MediTextField
import com.mgm.medimobile.ui.components.templates.ScreenLayout
import com.mgm.medimobile.viewmodel.MediMobileViewModel
import androidx.compose.material3.Checkbox

@Composable
fun LoginScreen(navController: NavController, viewModel: MediMobileViewModel) {

    // variables to hold user input for username and password

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }

    val isLoading = viewModel.isLoading.collectAsState().value

    // currently selected group event
    var group by remember { mutableStateOf<String?>(null) }
    val selectedEvent = viewModel.selectedEvent.collectAsState().value

    var errorText by remember { mutableStateOf<String>("") }
    var showErrorPopup by remember { mutableStateOf(false) }
    val loginResult by viewModel.loginResult.collectAsState()

    // controls which fields are selected
    val focusManager = LocalFocusManager.current

    // specific focus requesters for each field
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.errorFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { error ->
                errorText = error
                showErrorPopup = true
            }
    }

    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            result.onSuccess {
                navController.navigate("mainMenu")
                viewModel.clearLoginResult()
            }
        }
    }

    ScreenLayout(
        content = {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                AppTitle(modifier = Modifier.padding(top = 48.dp))

                Spacer(modifier = Modifier.weight(0.15f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                )
                {
                    BaseDropdown(
                        currentSelection = group ?: "",
                        options = selectedEvent?.userGroups?.toDisplayValues(),
                        dropdownLabel = "Service",
                        onSelectionChanged = { newLocation ->
                            if (newLocation != null) {
                                group = newLocation
                            }
                        },
                        notNullable = true,
                        modifier = Modifier.testTag("serviceDropdown")
                    )
                }

                // Login Form
                MediTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("username") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
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
                        .testTag("usernameTextField")
                )

                MediTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("password") },
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go,
                        autoCorrectEnabled = false
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
                        .testTag("passwordTextField")
                )

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(0.75f)
                ) {
                    Checkbox(
                        checked = showPassword,
                        onCheckedChange = { showPassword = it },
                        modifier = Modifier.testTag("showPasswordCheckbox")
                    )
                    Text(
                        text = "Show Password",
                        modifier = Modifier.testTag("showPasswordText")
                    )
                }

                // Login Button
                MediButton(
                    onClick = { viewModel.login(username, password, group ?: "") },
                    modifier = Modifier.testTag("loginButton")) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.weight(1f))

            }
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MediButton(
                    onClick = { navController.navigate("eventSelect") },
                    modifier = Modifier.testTag("eventSelectButton")
                ) {
                    Text(text = "Event Select")
                }
            }
        }
    )

    if (showErrorPopup) {
        ErrorPopup(
            errorTitle = "Login Failed",
            errorMessage = errorText,
            onDismiss = { showErrorPopup = false }
        )
    }


    // Freeze screen and show loading indicator when loading
    LoadingIndicator(isLoading, Modifier.testTag("loadingIndicator"))
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavController(LocalContext.current), viewModel = MediMobileViewModel())
}