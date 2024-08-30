package com.vpvn.mediaplayer.ui.home

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vpvn.mediaplayer.Screen
import com.vpvn.mediaplayer.Singleton
import com.vpvn.mediaplayer.ui.player.PlayerScreen
import com.vpvn.mediaplayer.ui.videolisting.VideoFilesScreen


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaPlayerApp() {
    val multiplePermission =
        rememberMultiplePermissionsState(permissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE))
    if (multiplePermission.allPermissionsGranted) {
        MediaPlayerNavHost(navController = rememberNavController())
    } else {
        PermissionHandlerScreen(multiplePermission)
    }
}

@Composable
fun MediaPlayerNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeBottomNavigationScreen(onItemClick = { pair ->
                Singleton.absolutePath = pair.second
                navController.navigate(
                    Screen.VideoListing.createRoute(
                        directoryName = pair.first,
                        absolutePath = pair.second
                    )
                )
            })
        }

        composable(route = Screen.VideoListing.route) {
            VideoFilesScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onItemClick = { uri ->
                    navController.navigate(
                        Screen.Player.createRoute(
                            itemUri = uri
                        )
                    )
                })
        }

        composable(route = Screen.Player.route) {
            PlayerScreen()
        }
    }
}
