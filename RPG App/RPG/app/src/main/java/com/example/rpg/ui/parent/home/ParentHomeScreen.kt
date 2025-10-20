package com.example.rpg.ui.parent.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rpg.R
import com.example.rpg.data.model.User
import com.example.rpg.ui.Routes
import com.example.rpg.ui.theme.RPGTheme

//Placeholder data until ViewModel implemented
//val family = listOf(
//    Family("Timmy", 3, 0.1F),
//    Family("John", 2, 0.5F),
//    Family("Bradford", 1, 0.1F)
//)

//In screens with a NavBar that need to be able to navigate to both types of screens (with and without bar)
//you need to add both navController and overlayNavController to the parameters

//Displays each family member below the image of the parent
@Composable
fun ParentHomeScreen(
    navController: NavHostController,
    overlayNavController: NavHostController,
    viewModel: ParentHomeScreenViewModel = hiltViewModel(),
    parentId: String,
    modifier: Modifier = Modifier,
){
    val children by viewModel.children.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChildren(parentId)
    }

    Column(
        modifier = Modifier.padding(top = 85.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.padding(top = 85.dp),
            onClick = { navController.navigate(Routes.ParentLandingScreen.route) }) {
            Text(text = "Landing Page")
        }
        Image(
            painter = painterResource(id = R.drawable.baseline_person_24),
            contentDescription = "Photo of Avatar",
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
        LazyColumn{
            items(children, key = { it.id }) { child ->
                CardView(child)
            }
        }
    }
}

//This function takes the members information and displays it in card view
@Composable
fun CardView(user: User) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Photo of child avatar",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Column {

                Text(
                    text = user.firstname + " " + user.lastname,
                    modifier = Modifier.padding(top = 16.dp, bottom = 20.dp)
                )

                // TODO new respository
//                ProgressIndicator(
//                    progress = user.lvlProgress
//                )

            }
        }
    }
}

//Progress bar displays current level progress
@Composable
fun ProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier
){
    LinearProgressIndicator(
    progress = { progress },
    modifier = modifier,
    )
}

//@Preview
//@Composable
//fun PreviewParentHomeScreen(){
//    RPGTheme {
//        ParentHomeScreen(navController = rememberNavController(), overlayNavController = rememberNavController())
//    }
//}