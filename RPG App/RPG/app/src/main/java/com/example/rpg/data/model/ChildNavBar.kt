package com.example.rpg.data.model

import com.example.rpg.R
import com.example.rpg.ui.Routes

// class that holds data for the buttons in the parent's bottom nav bar, add here to add buttons to bottom bar
enum class ChildNavBar(val route: String, val title: String, val icon: Int) {
    ChildGame(Routes.ChildGameScreen.route, "Game", R.drawable.baseline_videogame_asset_24),
    ChildQuest(Routes.ChildQuestScreen.route, "Quest", R.drawable.baseline_menu_book_24),
    ChildHome(Routes.ChildHomeScreen.route, "Home", R.drawable.baseline_home_24),
    ChildSocial(Routes.ChildSocialScreen.route, "Social", R.drawable.outline_group_24),
    ChildSettings(Routes.ChildSettingsScreen.route, "Settings", R.drawable.baseline_settings_24),
}