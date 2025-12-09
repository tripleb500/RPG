package com.example.rpg.ui.child.border

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rpg.R
import com.example.rpg.data.model.Border
import com.example.rpg.data.model.User
import com.example.rpg.ui.child.home.ChildHomeScreenViewModel

val levelBorders = mutableStateListOf(
    Border(0, "Hello World", R.drawable.tier0),
    Border(5, "Level 5", R.drawable.tier1),
    Border(10, "Level 10", R.drawable.tier2),
    Border(25, "Level 25", R.drawable.tier3),
    Border(50, "Level 50", R.drawable.tier4),
    Border(100, "Level 100", R.drawable.tier5),
    Border(200, "Level 200", R.drawable.tier6)
)

@Composable
fun ChildCustomizeBorderDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onAvatarBorderSelect: (Int) -> Unit,
    viewModel: ChildHomeScreenViewModel
){
    val level by viewModel.currentLevel.collectAsState()
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.8f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ){
                items(levelBorders){Border ->
                    CardView(level, Border,
                        onDismissRequest = {onDismissRequest()},
                onAvatarBorderSelect = { id ->
                    onAvatarBorderSelect(id)
                })
                }
            }
        }
    }
}

@Composable
fun CardView(
    level: Int,
    border: Border,
    onDismissRequest: () -> Unit,
    onAvatarBorderSelect: (Int) -> Unit
    ){
    if(level >= border.id){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFBBDEFB), shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp)
                    .clickable{
                        onAvatarBorderSelect(border.fileId)
                        onDismissRequest()
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    AsyncImage(
                        model = border.fileId,
                        contentDescription = "Avatar Border",
                        modifier = Modifier
                            //.padding(end = 12.dp)
                            .size(100.dp),
                        //.clip(CircleShape), // This makes it a perfect circle
                        //.clickable { showProfilePictureDialog = true },
                    )
                    Card(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Text(border.description)
                        }

                    }

                }

            }
        }

    }
}