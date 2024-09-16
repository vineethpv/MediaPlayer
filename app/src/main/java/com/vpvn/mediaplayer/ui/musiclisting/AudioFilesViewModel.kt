package com.vpvn.mediaplayer.ui.musiclisting

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpvn.mediaplayer.NavRouteConstants
import com.vpvn.mediaplayer.extension.stateInWhileSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class AudioFilesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val directoryNameState: StateFlow<String> = savedStateHandle.getStateFlow(NavRouteConstants.DIRECTORY_NAME, "")
    private val absolutePath = savedStateHandle.get<String>(NavRouteConstants.ABSOLUTE_PATH) ?: ""
    val audioFilesUiState =
        MutableStateFlow(getAudioFilesFrom(absolutePath)).stateInWhileSubscribed(initialValue = AudioFilesUiState.Loading)

    private fun getAudioFilesFrom(folderPath: String): AudioFilesUiState {
        println("vineeth - getAudioFilesFrom :: $folderPath")
        val videoFiles = mutableListOf<AudioFile>()
        viewModelScope.launch(Dispatchers.IO) {
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATE_ADDED
            )
            val sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC"
            val selection = MediaStore.Audio.Media.DATA + " like?"
            val selectionArgs = arrayOf("%$folderPath%")

            context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
                ?.let { cursor ->
                    val columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val columnIndexDisplayName = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                    val columnIndexDuration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val columnIndexDateAdded = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(columnIndexID)
                        val displayName = cursor.getString(columnIndexDisplayName)
                        val duration = cursor.getLong(columnIndexDuration)
                        val dateAdded = cursor.getLong(columnIndexDateAdded)

                        videoFiles.add(
                            AudioFile(
                                id,
                                buildUrl(folderPath, displayName),
                                formatDisplayName(displayName),
                                formatDuration(duration),
                                formatDate(dateAdded)
                            )
                        )
                    }
                    cursor.close()
                }
        }
        return AudioFilesUiState.Success(videoFiles)
    }

    private fun formatDate(dateInSeconds: Long): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateInSeconds * 1000))
    }

    private fun buildUrl(path: String, name:String) = "$path/$name"

    private fun formatDisplayName(displayName: String) = displayName.substringBefore(".")

    private fun formatDuration(duration: Long): String {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) % 60
        )
    }
}

data class AudioFile(
    val id: Int,
    val url: String,
    val displayName: String,
    val duration: String,
    val date: String
)

sealed interface AudioFilesUiState {
    data class Success(val videoFiles: List<AudioFile>) : AudioFilesUiState
    data object Loading : AudioFilesUiState
}