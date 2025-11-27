package com.example.rpg.ui.child.achievements

data class questAchievements(
    val achName: String,
//    val achDate: String, //date it was gotten
    val achDesc: String, //description of achievement
    val achAmount: Int
)

data class levelAchievements(
    val achName: String,
//    val achDate: String,
    val achDesc: String,
    val achAmount: Int
)
