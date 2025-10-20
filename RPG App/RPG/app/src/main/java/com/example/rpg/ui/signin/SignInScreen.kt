package com.example.rpg.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.ui.Routes
import com.example.rpg.ui.theme.RPGTheme


@Composable
fun SignInScreen(viewModel: SignInViewModel = hiltViewModel(),
                 navController: NavController) {
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    SignInScreenContent(
        signIn = {email, password ->
            viewModel.signIn(email, password) {success, role ->
                if (success && role != null) {
                    when (role.lowercase()) {
                        "parent" -> navController.navigate(Routes.ParentLandingScreen.route) {
                            popUpTo(Routes.SignInScreen.route) {inclusive = true }
                        }
                        "child" ->  navController.navigate(Routes.ChildLandingScreen.route) {
                            popUpTo(Routes.SignInScreen.route) {inclusive = true }
                        }
                    }
                }
            }
        },
        errorMessage = errorMessage,
        navController = navController
        )
}
@Composable
fun SignInScreenContent (
    signIn: (String, String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    navController: NavController
) {

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
            onValueChange = {password = it},
            label = {Text (text = "Password")}
        )

        Spacer(Modifier.height(8.dp))

        errorMessage?.let {
            Text(text = it,
                color = Color.Red,
                modifier = modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            // Add firebase logic
            signIn(email,password)
        }) {
            Text(text = "Sign in")
        }

        TextButton(onClick = {
            navController.navigate(Routes.SignUpScreen.route)
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
        SignInScreenContent(
            signIn = {_,_ ->}, errorMessage = null,
            navController = rememberNavController()
        )
    }
}