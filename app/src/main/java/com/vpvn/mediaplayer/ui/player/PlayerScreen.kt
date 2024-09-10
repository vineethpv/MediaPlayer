package com.vpvn.mediaplayer.ui.player

import android.content.pm.ActivityInfo
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.vpvn.mediaplayer.R
import com.vpvn.mediaplayer.extension.findActivity

@Composable
fun PlayerScreen(
    viewmodel: PlayerViewmodel = hiltViewModel()
) {
    val videoInfoState by viewmodel.videoInfoState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    EnableSystemUi(show = false)
    when (val state = videoInfoState) {
        is VideoPlayerUiState.Success -> {
            println("vineeth - PlayerScreen :: uri - ${state.videoInfo.uri} :: orientation - ${state.videoInfo.orientation}")

            if (state.videoInfo.orientation != ORIENTATION.PORTRAIT)
                ChangeScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

            Surface {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val exoPlayer = remember {
                        ExoPlayer.Builder(context).build().apply {
                            setMediaItem(MediaItem.fromUri(state.videoInfo.uri))
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

        VideoPlayerUiState.LoadingFailed -> {
            Toast.makeText(context, stringResource(id = R.string.video_loading_failed), Toast.LENGTH_LONG).show()
        }

        VideoPlayerUiState.Loading -> {
            //Toast.makeText(context, stringResource(id = R.string.loading), Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun ChangeScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(key1 = orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose { }
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation
            activity.requestedOrientation = originalOrientation
        }
    }
}

@Composable
fun EnableSystemUi(show: Boolean) {
    val context = LocalContext.current
    context.findActivity()?.let {
        if (show) it.actionBar?.show() else it.actionBar?.hide()

        //Hide the status bars
        it.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, show)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {
                window.insetsController?.apply {
                    if (show) show(WindowInsets.Type.statusBars())
                    else {
                        hide(WindowInsets.Type.statusBars())
                        systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                }
            }
        }
    }
}

private fun getLifeCycleObserver(player: ExoPlayer): LifecycleObserver {
    println("vineeth - PlayerScreen :: getLifeCycleObserver()")
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

            }
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
                /*Lifecycle.Event.ON_RESUME -> TODO()
                  Lifecycle.Event.ON_PAUSE -> TODO()
                  Lifecycle.Event.ON_ANY -> TODO()*/
            }
        }
    }
}