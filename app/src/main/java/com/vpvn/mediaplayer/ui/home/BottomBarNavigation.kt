package com.vpvn.mediaplayer.ui.home

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    data object Home : BottomDestination("home", R.drawable.ic_launcher_foreground, "Home")
    data object Music : BottomDestination("music", R.drawable.ic_launcher_foreground, "Music")
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
                        contentDescription = item.title, modifier = Modifier.size(56.dp)
                    )
                },
                label = { Text(text = item.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
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