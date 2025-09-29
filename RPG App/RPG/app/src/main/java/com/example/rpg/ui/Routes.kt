package com.example.rpg.ui

sealed class Routes(val route: String) {
    object ParentLandingScreen : Routes("parentLanding")
    object ChildLandingScreen : Routes("childLanding")

    object ChildHomeScreen : Routes(route = "childHome")
}