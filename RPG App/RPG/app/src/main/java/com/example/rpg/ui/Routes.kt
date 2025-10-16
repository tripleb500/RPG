package com.example.rpg.ui

sealed class Routes(val route: String) {
    // routes for account management
    object SignInScreen : Routes ("signInScreen")

    // routes for parent screens
    object ParentLandingScreen : Routes("parentLanding")

    object ParentAddChildDialog : Routes("parentAddChild")
    object ParentQuestScreen : Routes("parentQuest")

    // START NOTE
    // Bryan Note: Can we make another navgraph for overview, assign, ongoing etc.?
    object ParentQuestOverviewScreen : Routes("parentQuestOverview")
    object ParentQuestAssignScreen : Routes("parentQuestAssign")
    object ParentQuestOngoingScreen : Routes("parentQuestOngoing")
    object ParentQuestPendingScreen : Routes("parentQuestPending")
    object ParentQuestCompletedScreen : Routes("parentQuestPending")
    // END NOTE

    object ParentHomeScreen : Routes("parentHome")
    object ParentStatsScreen : Routes("parentStats")
    object ParentSettingsScreen : Routes("parentSettings")

    object ParentModerationMainScreen : Routes("parentModerationMain")
    object ParentScreentimeScreen : Routes("parentScreentime")
    object ParentAccountScreen : Routes("parentAccount")
    object ParentNavGraph : Routes("ParentNavGraph")

    // routes for child screens
    object ChildLandingScreen : Routes("childLanding")
    object ChildHomeScreen : Routes("childHome")
    object ChildGameScreen :Routes("childGame")
    object ChildSettingsScreen :Routes("childSettings")
    object ChildSocialScreen :Routes("childSocial")
    object ChildQuestScreen :Routes("childQuest")
    object ChildNavGraph : Routes("ChildNavGraph")
}