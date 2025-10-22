package com.example.rpg.data.model

import com.example.rpg.R
import com.example.rpg.ui.Routes

// class that holds data for the buttons in the parent's bottom nav bar, add here to add buttons to bottom bar
enum class ParentNavBar(val route: String, val title: String, val icon: Int) {
    ParentQuest(Routes.ParentQuestScreen.route, "Quest", R.drawable.baseline_menu_book_24),
    ParentHome(Routes.ParentHomeScreen.route, "Home", R.drawable.baseline_home_24),
    ParentStats(Routes.ParentStatsScreen.route, "Stats", R.drawable.baseline_auto_graph_24),
    ParentSettings(Routes.ParentSettingsScreen.route, "Settings", R.drawable.baseline_settings_24),
}