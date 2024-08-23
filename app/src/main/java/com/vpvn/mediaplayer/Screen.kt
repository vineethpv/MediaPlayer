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
        route = "videoListing/{directoryPath}",
        navArguments = listOf(navArgument("directoryPath") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(directoryPath: String) = "videoListing/${directoryPath}"
    }
}