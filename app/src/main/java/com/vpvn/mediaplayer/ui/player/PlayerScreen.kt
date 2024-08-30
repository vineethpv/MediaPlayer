package com.vpvn.mediaplayer.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun PlayerScreen(
    viewmodel: PlayerViewmodel = hiltViewModel()
) {
    val uri by viewmodel.mediaItemUriState.collectAsStateWithLifecycle()
    println("vineeth - PlayerScreen :: uri - $uri")

    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(uri))
                    prepare()
                    playWhenReady = true
                    repeatMode = Player.REPEAT_MODE_ONE
                }
            }
            PlayerSurface(
                player = exoPlayer,
                surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            LocalLifecycleOwner.current.lifecycle.addObserver(getLifeCycleObserver(exoPlayer))
        }
    }
}

private fun getLifeCycleObserver(player: ExoPlayer): LifecycleObserver {
    println("vineeth - PlayerScreen :: getLifeCycleObserver()")
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                println("vineeth - PlayerScreen :: getLifeCycleObserver - ON_START")
                if (player.isPlaying.not()) {
                    player.play()
                }
            }

            Lifecycle.Event.ON_STOP -> {
                println("vineeth - PlayerScreen :: getLifeCycleObserver - ON_STOP")
                player.pause()
            }

            Lifecycle.Event.ON_DESTROY -> {
                println("vineeth - PlayerScreen :: getLifeCycleObserver - ON_DESTROY")
                player.release()
            }

            else -> {
                /*Lifecycle.Event.ON_CREATE -> TODO()
                    Lifecycle.Event.ON_RESUME -> TODO()
                    Lifecycle.Event.ON_PAUSE -> TODO()
                    Lifecycle.Event.ON_ANY -> TODO()*/
            }
        }
    }
}