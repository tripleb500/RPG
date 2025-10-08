package com.example.rpg.data.model

import com.example.rpg.R
import com.example.rpg.ui.Routes

enum class ParentNavBar(val route: String, val title: String, val icon: Int, ) {
    parentHome(Routes.ParentHomeScreen.route, "Home", R.drawable.baseline_person_24),
    //testing purposes delete later:
    childHome(Routes.ChildHomeScreen.route, "Child Home", R.drawable.outline_photo_camera_back_24),

    //parentHome(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
    //parentQuest(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
    //parentProfile(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
    //parentSocial(Routes.ParentHomeScreen.route, "Home", R.drawable.rpg_logo_parent),
}