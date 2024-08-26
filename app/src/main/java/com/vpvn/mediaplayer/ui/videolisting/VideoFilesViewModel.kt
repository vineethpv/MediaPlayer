package com.vpvn.mediaplayer.ui.videolisting

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VideoFilesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val directoryNameState: StateFlow<String> = savedStateHandle.getStateFlow("directoryPath", "")
    val videoFilesUiState = directoryNameState
        .map { VideoFilesUiState.Success(getVideoFilesFrom(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(0),
            initialValue = VideoFilesUiState.Loading
        )

    private fun getVideoFilesFrom(folderPath: String): List<VideoFiles> {
        val videoFiles = mutableListOf<VideoFiles>()
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

                        videoFiles.add(VideoFiles(id, displayName, duration.toString()))
                    }

                    cursor.close()
                }
        }
        return videoFiles
    }
}

data class VideoFiles(
    val id: Int,
    val displayName: String,
    val duration: String
)

sealed interface VideoFilesUiState {
    data class Success(val videoFiles: List<VideoFiles>) : VideoFilesUiState
    data object Loading : VideoFilesUiState
}