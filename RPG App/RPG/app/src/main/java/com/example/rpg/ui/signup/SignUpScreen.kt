package com.example.rpg.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rpg.ui.theme.RPGTheme
import kotlin.math.sign

/**
 * TODO:
 * Fix alignment of family role options; Overall fix formatting.
 * Implement Viewmodel (Ask group about potential use of HiltViewModel?)
 * Add firebase logic for signup button
 * Add nav logic to correct landing page
 * Add nav logic to sign-in screen
 */

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()
    SignUpScreenContent(
        signUp = { email, password ->
            viewModel.signUp(email, password)
        }

    )
}
@Composable
fun SignUpScreenContent(
    signUp: (String, String,) -> Unit
) {
    var username by remember {mutableStateOf("")}
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var selectedFamilyRole by remember {mutableStateOf("")}



    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center ) {

        Text(text = "Create Account", fontSize = 32.sp)

        Spacer(Modifier.height(16.dp))

        // Username input field
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = {Text (text = "Username")},
            )

        Spacer(Modifier.height(8.dp))

        // Email input field
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text ("Email")}

        )

        Spacer(Modifier.height(8.dp))

        // Password  input field
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text ("Password")}
        )

        Spacer(Modifier.height(16.dp))


        /**
         * RADIO BUTTON Functionality
         * User inputs family role; either "parent" or "child"
         * Option should dictate which homepage user is directed to when signing up.
         */
        Text(text = "Select a family role:", fontSize = 16.sp)

        Row(modifier = Modifier) {
            val radioOptions = listOf("Parent", "Child")
            val (selectedOption, onOptionSelected) = remember {mutableStateOf(radioOptions[0])}

            Column(Modifier.selectableGroup()) {
                radioOptions.forEach {text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {onOptionSelected(text)  },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null
                        )
                        Text(
                            text = text
                        )
                    }
                }
            }
        }


        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            // Add Firebase auth logic when completed
            signUp(
                email,
                password,
            )
        }) {
            Text(text = "Sign Up")
        }

        TextButton(onClick = {
            // Add nav logic to login page
        }) {
            Text(text = "Have an account? Sign-in here")
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewSignUpScreen(){
    RPGTheme {
        SignUpScreenContent(
            signUp = {_,_, ->}
        )
    }
}
