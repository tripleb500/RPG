package com.example.rpg.data.model

import com.example.rpg.R
import com.example.rpg.ui.Routes

//class that holds data for the buttons in the parent's bottom nav bar, add here to add buttons to bottom bar
enum class ChildNavBar(val route: String, val title: String, val icon: Int) {

//    childGame(Routes.ChildGameScreen.route, "Child Game", R.drawable.game), // TODO
//    childQuest(Routes.ChildQuestScreen.route, "Child Quest", R.drawable.quest), // TODO
    ChildHome(Routes.ChildHomeScreen.route, "Child Home", R.drawable.baseline_home_24),
    ChildProfile(Routes.ChildProfileScreen.route, "Child Profile", R.drawable.baseline_person_24),
//    childSocial(Routes.ChildSocialScreen.route, "Home", R.drawable.rpg_logo_child), // TODO
}