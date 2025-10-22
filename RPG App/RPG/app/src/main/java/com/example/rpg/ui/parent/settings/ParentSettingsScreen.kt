package com.example.rpg.ui.parent.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.ui.Routes
import com.example.rpg.ui.theme.RPGTheme


@Composable
fun ParentSettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentSettingsViewModel = hiltViewModel()
) {
    val settingsOptions = listOf(
        1 to "Account",
        2 to "Notifications",
        3 to "Privacy",
        4 to "Appearance",
        5 to "About",
        6 to "Logout"
    )
    Column(
        modifier = Modifier.padding(top = 85.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Parent Settings Screen")

        Spacer(Modifier.height(16.dp))


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(settingsOptions) { (id, name) ->
                ClickableCard(
                    title = name, onClick = {
                        viewModel.onCardClicked(id)
                        navController.navigate(Routes.SignInScreen.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    })
            }
        }

    }
}

@Composable
fun ClickableCard(
    title: String, onClick: () -> Unit
) {
    Card(

        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .padding(12.dp), onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Photo of default avatar",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title, textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun PreviewParentQuestScreen() {
    RPGTheme {
        ParentSettingsScreen(
            navController = rememberNavController(), overlayNavController = rememberNavController()
        )
    }
}