package com.example.rpg.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpg.ui.theme.RPGTheme


/**
 * TODO:
 * Add firebase logic for sign-in button
 * Add navigation route to Sign-Up
 * Stylize the screen when time is available
 */
@Composable
fun SignInScreen (modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}

    Column (
        modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Text(text = "Sign-In", fontSize = 32.sp)

        Spacer(Modifier.height(16.dp))

        // Email text input
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text (text = "Email")}
        )

        Spacer(Modifier.height(8.dp))

        // Password text input
        OutlinedTextField(
            value = password,
            onValueChange = {password},
            label = {Text (text = "Password")}
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            // Add firebase logic
        }) {
            Text(text = "Sign in")
        }

        TextButton(onClick = {
            // Add nav logic to Sign-Up
        }) {
            Text(text = "Don't have an account? Sign-up here")
        }
    }
}


@Preview (
    showBackground = true,
    showSystemUi = true)
@Composable
fun PreviewSignInScreen () {
    RPGTheme {
        SignInScreen()
    }
}