package com.vpvn.mediaplayer.ui.videolisting

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpvn.mediaplayer.NavRouteConstants
import com.vpvn.mediaplayer.Singleton
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VideoFilesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val directoryNameState: StateFlow<String> = savedStateHandle.getStateFlow(NavRouteConstants.DIRECTORY_NAME, "")
    private val absolutePath = Singleton.absolutePath //savedStateHandle.get<String>(NavRouteConstants.ABSOLUTE_PATH)
    val videoFilesUiState = MutableStateFlow(VideoFilesUiState.Success(getVideoFilesFrom(absolutePath)))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(0),
            initialValue = VideoFilesUiState.Loading
        )

    private fun getVideoFilesFrom(folderPath: String): List<VideoFile> {
        println("vineeth - getVideoFilesFrom :: $folderPath")
        val videoFiles = mutableListOf<VideoFile>()
        viewModelScope.launch(Dispatchers.IO) {
            val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
            )
            val sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC"
            val selection = MediaStore.Video.Media.DATA + " like?"
            val selectionArgs = arrayOf("%$folderPath%")

            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
                ?.let { cursor ->
                    val columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                    val columnIndexDisplayName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                    val columnIndexDuration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(columnIndexID)
                        val displayName = cursor.getString(columnIndexDisplayName)
                        val duration = cursor.getInt(columnIndexDuration)

                        val url = "$folderPath/$displayName"
                        videoFiles.add(VideoFile(id, url, displayName.substringBefore("."), duration.toString()))
                    }

                    cursor.close()
                }
        }
        return videoFiles
    }
}

data class VideoFile(
    val id: Int,
    val url: String,
    val displayName: String,
    val duration: String
)

sealed interface VideoFilesUiState {
    data class Success(val videoFiles: List<VideoFile>) : VideoFilesUiState
    data object Loading : VideoFilesUiState
}