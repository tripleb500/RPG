package com.example.rpg.ui.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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


// "Container", connects to viewmodel; defines what logic to pass down to UI
@Composable
fun SignUpScreen(viewModel: SignUpViewModel = hiltViewModel(),
                 navController: NavController) {

    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    SignUpScreenContent(
        signUp = {firstname, lastname, email, password, username, role ->  // Defines what happens when user submits sign-up.
            viewModel.signUp(  // Calls SignUpViewModel signUp function.
                firstname,
                lastname,
                email,
                password,
                username = username,
                role = role,
                onSuccess = { success, userRole ->  // Post Sign-up behavior.
                    if(success && userRole != null) {  // Navigate user to correct homepage and functionalities based on the family role they selected.
                        when(userRole.lowercase()) {
                            "parent" -> navController.navigate(Routes.ParentLandingScreen.route) {
                                popUpTo(Routes.SignUpScreen.route) { inclusive = true }  // Ensures that user cannot come back to sign-up screen
                            }
                            "child" -> navController.navigate(Routes.ChildLandingScreen.route) {
                                popUpTo(Routes.SignUpScreen.route) { inclusive = true }
                            }
                        }

                    }
                })
        },
        errorMessage = errorMessage,
        navController = navController
    )
}

// Pure UI, builds screen layout, responds to user input.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenContent (
    signUp: (String, String, String, String, String, String) -> Unit,
    errorMessage: String?,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var firstname by remember {mutableStateOf("")}
    var lastname by remember {mutableStateOf("")}

    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var username by remember {mutableStateOf("")}
    
    val roleOptions = listOf("Parent", "Child")
    var expanded by remember { mutableStateOf(false) }  // Whether the dropdown menu is open.
    var selectedRole by remember {mutableStateOf(roleOptions[0])}  // Selected dropdown role.

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center ) {

        Text(text = "Create Account", fontSize = 32.sp)

        Spacer(Modifier.height(16.dp))

        // Firstname input field
        OutlinedTextField(
            value = firstname,
            onValueChange = {firstname = it},
            label = {Text ("First Name")}
        )

        // Lastname input field
        OutlinedTextField(
            value = lastname,
            onValueChange = {lastname = it},
            label = {Text ("Last Name")}
        )
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

        Spacer(Modifier.height(8.dp))

        // Username input field.
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = {Text ("Username")}
        )

        Spacer(Modifier.height(8.dp))

        // Dropdown menu for family roles (parent and child).
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }

        ) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {},
                label = {Text("Select Role" ) },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded)},
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roleOptions.forEach {role ->
                    DropdownMenuItem(
                        text = { Text(role) },
                        onClick = {
                            selectedRole = role
                            expanded = false }

                    )
                }
            }
        }

        // If there is error with text fields error will display.
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Creates user's account, info is sent to Firebase Authentication if successful.
        Button(onClick = {
            signUp (
                firstname,
                lastname,
                email,
                password,
                username,
                selectedRole
            )
        }) {
            Text(text = "Sign Up")
        }

        TextButton(onClick = {
            navController.navigate(Routes.SignInScreen.route)
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
            signUp = {_,_,_,_,_,_ ->}, errorMessage = null,
            navController = rememberNavController()
        )
    }
}
