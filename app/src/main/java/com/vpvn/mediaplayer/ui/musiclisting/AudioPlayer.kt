package com.vpvn.mediaplayer.ui.musiclisting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.vpvn.mediaplayer.R

@Composable
fun AudioPlayer(
    exoPlayer: ExoPlayer,
    audioFile: AudioFile
) {
    println("vineeth:: AudioPlayer")

    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var isPlaying by remember { mutableStateOf(true) }
            exoPlayer.addListener(
                object : Player.Listener {
                    override fun onIsPlayingChanged(value: Boolean) {
                        isPlaying = value
                    }
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                IconButton(onClick = {
                    if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                        isPlaying = false
                    } else {
                        exoPlayer.play()
                        isPlaying = true
                    }
                }) {
                    Icon(
                        painter = painterResource(
                            id = if (isPlaying) R.drawable.baseline_pause_circle_24
                            else R.drawable.baseline_play_circle_24
                        ),
                        contentDescription = "play",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(25.dp)
                    )
                }

                /*Slider(
                    value = if (currentPosTemp == 0f) currentMediaProgress else currentPosTemp,
                    onValueChange = { currentPosTemp = it },
                    onValueChangeFinished = {
                        currentMediaProgress = currentPosTemp
                        currentPosTemp = 0f
                        onSeekBarPositionChanged(currentMediaProgress.toLong())
                    },
                    valueRange = 0f..audioFile.duration.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )*/
            }
            HorizontalDivider()
        }
    }
}

fun getLifeCycleObserver(player: ExoPlayer): LifecycleObserver {
    println("vineeth - AudioPlayerScreen :: getLifeCycleObserver()")
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                println("vineeth - AudioPlayerScreen :: getLifeCycleObserver - ON_CREATE")
            }

            Lifecycle.Event.ON_START -> {
                println("vineeth - AudioPlayerScreen :: getLifeCycleObserver - ON_START")
                if (player.isPlaying.not()) {
                    player.play()
                }
            }

            Lifecycle.Event.ON_STOP -> {
                println("vineeth - AudioPlayerScreen :: getLifeCycleObserver - ON_STOP")
                player.pause()
            }

            Lifecycle.Event.ON_DESTROY -> {
                println("vineeth - AudioPlayerScreen :: getLifeCycleObserver - ON_DESTROY")
                player.release()
            }

            else -> {}
        }
    }
}