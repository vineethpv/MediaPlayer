package com.vpvn.mediaplayer.ui.home

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vpvn.mediaplayer.R

sealed class BottomDestination(var route: String, var icon: Int, var title: String) {
    data object Home : BottomDestination("home", R.drawable.baseline_ondemand_video_24, "Video")
    data object Music : BottomDestination("music", R.drawable.baseline_music_video_24, "Music")
}

@Composable
fun BottomNavContent(navController: NavHostController, onItemClick: (String, String) -> Unit) {
    NavHost(navController, startDestination = BottomDestination.Home.route) {
        composable(BottomDestination.Home.route) {
            HomeScreen(onItemClick = onItemClick)
        }
        composable(BottomDestination.Music.route) {
            MusicScreen(onItemClick = onItemClick)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val destinations = listOf(BottomDestination.Home, BottomDestination.Music)

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        destinations.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(56.dp).alpha(0.5f)
                    )
                },
                label = { Text(text = item.title) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}