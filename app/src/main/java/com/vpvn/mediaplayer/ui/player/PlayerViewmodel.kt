package com.vpvn.mediaplayer.ui.player

import android.media.MediaMetadataRetriever
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpvn.mediaplayer.NavRouteConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mediaItemUri = savedStateHandle.get<String>(NavRouteConstants.MEDIA_ITEM_URI) ?: ""
    private val _videoInfoState: MutableStateFlow<VideoPlayerUiState> = MutableStateFlow(VideoPlayerUiState.Loading)
    val videoInfoState = _videoInfoState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(0),
        initialValue = VideoPlayerUiState.Loading
    )

    init {
        extractVideoInfo(mediaItemUri)
    }

    private fun extractVideoInfo(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _videoInfoState.value = getVideoInfoState(uri)
        }
    }

    private fun getVideoInfoState(uri: String): VideoPlayerUiState {
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever().apply { setDataSource(uri) }
            val rotation = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
            val width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            println("vineeth - getVideoInfoState :: rotation - $rotation  width - $width  height - $height")
            return VideoPlayerUiState.Success(VideoInfo(uri, getOrientation(rotation)))
        } catch (e: Exception) {
            return VideoPlayerUiState.LoadingFailed
        }
    }

    private fun getOrientation(rotation: String?) = when (rotation) {
        "0", "360" -> ORIENTATION.PORTRAIT
        "180", "270" -> ORIENTATION.LANDSCAPE
        else -> ORIENTATION.PORTRAIT
    }

}

data class VideoInfo(
    val uri: String,
    val orientation: ORIENTATION,
)

enum class ORIENTATION {
    LANDSCAPE, PORTRAIT
}

sealed interface VideoPlayerUiState {
    data class Success(val videoInfo: VideoInfo) : VideoPlayerUiState
    data object Loading : VideoPlayerUiState
    data object LoadingFailed : VideoPlayerUiState
}