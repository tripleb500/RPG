package com.example.rpg.ui

sealed class Routes(val route: String) {
    object ParentLandingScreen : Routes("parentLanding")
    object ChildLandingScreen : Routes("childLanding")

    object ChildHomeScreen : Routes("childHome")

    object ChildGameScreen :Routes("childGame")

    object ChildProfileScreen :Routes("childProfile")

    object ChildLeaderboardScreen :Routes("childLeaderboard")

    object ChildQuestScreen :Routes("childQuest")

    object ParentHomeScreen : Routes("parentHome")

}