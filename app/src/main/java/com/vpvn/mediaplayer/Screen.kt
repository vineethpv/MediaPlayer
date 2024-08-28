package com.vpvn.mediaplayer

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")

    data object VideoListing : Screen(
        route = "videoListing/{${NavRouteConstants.DIRECTORY_NAME}}",
        navArguments = listOf(
            navArgument(NavRouteConstants.DIRECTORY_NAME) { type = NavType.StringType })
    ) {
        fun createRoute(directoryName: String, absolutePath: String) =
            "videoListing/${directoryName}"
    }
}