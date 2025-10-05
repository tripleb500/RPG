package com.example.rpg.ui

sealed class Routes(val route: String) {
    object ParentLandingScreen : Routes("parentLanding")
    object ChildLandingScreen : Routes("childLanding")

    object ChildHomeScreen : Routes("childHome")

    object ChildProfileScreen :Routes("childProfile")

    object ChildLeaderboardScreen :Routes("childLeaderboard")

    object ChildQuestScreen :Routes("childQuest")

    object ParentHomeScreen : Routes(route = "parentHome")

}