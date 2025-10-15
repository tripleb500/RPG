package com.example.rpg.ui.profilesetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpg.ui.theme.RPGTheme

@Composable
fun ProfileSetupScreen () {
    var username by remember { mutableStateOf("") }
    var familyRole by remember {mutableStateOf("") }


    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Profile Setup", fontSize = 32.sp)

        Spacer(Modifier.height(16.dp))

        // Username Input Field
        OutlinedTextField(
            value = username,
            onValueChange = {username = it },
            label = {Text(text = "Username")}
        )
        
        Spacer(Modifier.height(16.dp))

        Text(text = "Select Your Family Role:")


    }
}

@Preview(
    showBackground = true,
    showSystemUi = true)
@Composable
fun PreviewProfileSetupScreen() {
    RPGTheme {
        ProfileSetupScreen()
    }
}