package com.vpvn.mediaplayer.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeBottomNavigationScreen(onItemClick: (Pair<String, String>) -> Unit) {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = bottomNavController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                BottomNavContent(navController = bottomNavController, onItemClick)
            }
        }
    )
}