package com.example.rpg.data.model

import com.example.rpg.R
import com.example.rpg.ui.Routes

//class that holds data for the buttons in the parent's bottom nav bar, add here to add buttons to bottom bar
enum class ParentNavBar(val route: String, val title: String, val icon: Int) {
//    parentAdd(Routes.TODO(addchild package).route, "Parent Add", R.drawable.baseline_add_24), // TODO; this is overlay
//    parentQuest(Routes.ParentQuestScreen.route, "Parent Quest", R.drawable.rpg_logo_parent), // TODO
    parentHome(Routes.ParentHomeScreen.route, "Parent Home", R.drawable.baseline_home_24),
//    parentStatistics(Routes.ParentStatisticsScreen.route, "Parent Statistics", R.drawable.baseline_auto_graph_24), // TODO
//    parentSettings(Routes.ParentSettingsScreen.route, "Parent Settings", R.drawable.baseline_settings_24),
}