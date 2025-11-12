package com.example.rpg.ui

sealed class Routes(val route: String) {
    // routes for account management
    object SignUpScreen : Routes("signUpScreen")
    object SignInScreen : Routes("signInScreen")

    // routes for parent screens
    object ParentLandingScreen : Routes("parentLanding")

    object ParentQuestScreen : Routes("parentQuest")
    object ParentAddQuestScreen : Routes("parentAddQuest")
    object ParentCameraScreen : Routes("parentCameraScreen")

    // START NOTE
    // Bryan Note: Can we make another navgraph for overview, assign, ongoing etc.?
    object ParentQuestOverviewScreen : Routes("parentQuestOverview")
    object ParentQuestOngoingScreen : Routes("parentQuestOngoing")
    object ParentQuestPendingScreen : Routes("parentQuestPending")
    object ParentQuestCompletedScreen : Routes("parentQuestPending")
    // END NOTE

    object ParentHomeScreen : Routes("parentHome")
    object ParentStatsScreen : Routes("parentStats")
    object ParentSettingsScreen : Routes("parentSettings")
    object ParentChangeUsernameScreen : Routes("parentChangeUsername")
    object ParentChangeEmailScreen : Routes("parentChangeEmail")
    object ParentChangePasswordScreen : Routes("parentChangePassword")

    object ParentModerationMainScreen : Routes("parentModerationMain")
    object ParentScreentimeScreen : Routes("parentScreentime")
    object ParentAccountSettingsScreen : Routes("parentAccount")
    object ParentNavGraph : Routes("ParentNavGraph")

    // routes for child screens
    object ChildLandingScreen : Routes("childLanding")
    object ChildHomeScreen : Routes("childHome")
    object ChildGameScreen : Routes("childGame")
    object ChildSettingsScreen : Routes("childSettings")
    object ChildAccountSettingsScreen: Routes("childAccount")
    object ChildChangeUsernameScreen: Routes("childChangeUsername")
    object ChildChangeEmailScreen : Routes("childChangeEmail")
    object ChildChangePasswordScreen : Routes("childChangePassword")
    object ChildSocialScreen : Routes("childSocial")
    object ChildQuestScreen : Routes("childQuest")
    object ChildNavGraph : Routes("ChildNavGraph")
}