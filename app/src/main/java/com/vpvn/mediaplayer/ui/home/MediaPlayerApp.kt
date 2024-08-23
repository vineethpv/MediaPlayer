package com.vpvn.mediaplayer.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vpvn.mediaplayer.Screen
import com.vpvn.mediaplayer.ui.videolisting.VideoFilesScreen


@Composable
fun MediaPlayerApp() {
    MediaPlayerNavHost(navController = rememberNavController())
}

@Composable
fun MediaPlayerNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeBottomNavigationScreen(onItemClick = {
                navController.navigate(Screen.VideoListing.createRoute(directoryPath = it))
            })
        }

        composable(route = Screen.VideoListing.route) {
            VideoFilesScreen(onBackClick = {
                navController.navigateUp()
            })
        }
    }
}