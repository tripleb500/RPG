package com.example.rpg.ui

sealed class Routes(val route: String) {

    // routes for account management
    object SignInScreen : Routes ("signInScreen")
    // routes for parent screens
    object ParentLandingScreen : Routes("parentLanding")
    object ParentHomeScreen : Routes("parentHome")
    object ParentAddChildDialog : Routes("parentAddChild")
    object ParentQuestOverviewScreen : Routes("parentQuestOverview")
    object ParentQuestAssignScreen : Routes("parentQuestAssign")
    object ParentQuestOngoingScreen : Routes("parentQuestOngoing")
    object ParentQuestPendingScreen : Routes("parentQuestPending")
    object ParentQuestCompletedScreen : Routes("parentQuestPending")
    object ParentStatisticsScreen : Routes("parentStatistics")
    object ParentModerationMainScreen : Routes("parentModerationMain")
    object ParentScreentimeScreen : Routes("parentScreentime")
    object ParentAccountScreen : Routes("parentAccount")
    object ParentNavGraph : Routes("ParentNavGraph")

    // routes for child screens
    object ChildLandingScreen : Routes("childLanding")
    object ChildHomeScreen : Routes("childHome")
    object ChildGameScreen :Routes("childGame")
    object ChildProfileScreen :Routes("childProfile")
    object ChildLeaderboardScreen :Routes("childLeaderboard")
    object ChildQuestScreen :Routes("childQuest")
}