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
    object ParentHomeScreen : Routes("parentHome")
    object ParentStatsScreen : Routes("parentStats")
    object ParentSettingsScreen : Routes("parentSettings")
    object ParentChangeUsernameScreen : Routes("parentChangeUsername")
    object ParentChangeEmailScreen : Routes("parentChangeEmail")
    object ParentChangePasswordScreen : Routes("parentChangePassword")

    object ParentModerationMainScreen : Routes("parentModerationMain")
    object ParentScreentimeScreen : Routes("parentScreentime")
    object ParentAccountSettingsScreen : Routes("parentAccount")
    object ParentNotificationsScreen : Routes("parentNotifications")
    object ParentPaymentScreen : Routes("parentPayment")

    object ParentNavGraph : Routes("ParentNavGraph")

    // routes for child screens
    object ChildLandingScreen : Routes("childLanding")
    object ChildHomeScreen : Routes("childHome")
    object ChildGameScreen : Routes("childGame")
    object ChildSettingsScreen : Routes("childSettings")
    object ChildAccountSettingsScreen: Routes("childAccount")
    object ChildNotificationsScreen: Routes("childNotifications")
    object ChildChangeUsernameScreen: Routes("childChangeUsername")
    object ChildChangeEmailScreen : Routes("childChangeEmail")
    object ChildChangePasswordScreen : Routes("childChangePassword")
    object ChildSocialScreen : Routes("childSocial")
    object ChildQuestScreen : Routes("childQuest")
    object ChildNavGraph : Routes("ChildNavGraph")
}