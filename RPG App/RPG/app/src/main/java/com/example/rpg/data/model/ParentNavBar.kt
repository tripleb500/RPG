package com.example.rpg.data.model

import com.example.rpg.R
import com.example.rpg.ui.Routes

enum class ParentNavBar(val route: Any, val title: String, rpgLogoParent: Int, ) {
    parentHome(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
    //parentHome(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
    //parentQuest(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
    //parentProfile(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
    //parentSocial(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
}