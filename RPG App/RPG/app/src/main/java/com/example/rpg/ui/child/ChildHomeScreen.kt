package com.example.rpg.ui.child

import android.widget.ImageButton
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rpg.R
import com.example.rpg.ui.Routes

val people = listOf(
          Person("John", "Malone", 25),
           Person("DJ", "Malone", 25),
           Person("Louis", "Malone", 19),
           Person("Sam", "Malone", 20),
        )

@Composable
fun ChildHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.padding(top = 85.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //switch button
        Button(
            modifier = Modifier.padding(top = 85.dp),
            onClick = { navController.navigate(Routes.ParentLandingScreen.route) }) {
            Text(text = "Child")
        }
        LazyColumn {
            items(people){
                CardView(it)
            }
        }
        //logo
        /**Image(
            painter = painterResource(R.drawable.rpg_logo_child),
            contentDescription = null,
            modifier = modifier.padding(24.dp),
        )**/
    }
}
@Composable
fun CardView(person: Person) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Photo  of person",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Column {
                Text(
                    text = person.firstName,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = person.lastName,
                )
                Text(
                    text = "Age: " + person.age,
                )
            }
        }
    }
}