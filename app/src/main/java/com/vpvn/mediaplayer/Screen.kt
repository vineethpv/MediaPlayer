package com.vpvn.mediaplayer

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.vpvn.mediaplayer.extension.encodeUTF8

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen(route = "home")

    data object VideoListing : Screen(
        route = "videoListing/{${NavRouteConstants.DIRECTORY_NAME}}/{${NavRouteConstants.ABSOLUTE_PATH}}",
        navArguments = listOf(
            navArgument(NavRouteConstants.DIRECTORY_NAME) { type = NavType.StringType },
            navArgument(NavRouteConstants.ABSOLUTE_PATH) { type = NavType.StringType })
    ) {
        fun createRoute(directoryName: String, absolutePath: String) =
            "videoListing/${directoryName}/${absolutePath.encodeUTF8()}"
    }

    data object AudioListing : Screen(
        route = "audioListing/{${NavRouteConstants.DIRECTORY_NAME}}/{${NavRouteConstants.ABSOLUTE_PATH}}",
        navArguments = listOf(
            navArgument(NavRouteConstants.DIRECTORY_NAME) { type = NavType.StringType },
            navArgument(NavRouteConstants.ABSOLUTE_PATH) { type = NavType.StringType })
    ) {
        fun createRoute(directoryName: String, absolutePath: String) =
            "audioListing/${directoryName}/${absolutePath.encodeUTF8()}"
    }

    data object Player : Screen(
        route = "player/{${NavRouteConstants.MEDIA_ITEM_URI}}",
        navArguments = listOf(navArgument(NavRouteConstants.MEDIA_ITEM_URI) {
            type = NavType.StringType
        })
    ) {
        fun createRoute(itemUri: String) = "player/${itemUri.encodeUTF8()}"
    }
}

enum class MEDIA_TYPE {
    VIDEO, AUDIO
}