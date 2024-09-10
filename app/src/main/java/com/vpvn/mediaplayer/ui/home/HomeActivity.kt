package com.vpvn.mediaplayer.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vpvn.mediaplayer.ui.theme.MediaPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MediaPlayerTheme {
                MediaPlayerApp()
            }
        }
    }
}
