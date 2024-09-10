package com.vpvn.mediaplayer.ui.home

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _musicDirectoriesLiveData = MutableLiveData(emptyList<MusicDirectory>())
    val musicDirectoriesLiveData: LiveData<List<MusicDirectory>>
        get() = _musicDirectoriesLiveData

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _musicDirectoriesLiveData.postValue(getMusicFolders())
            }
        }
    }

    private fun getMusicFolders(): List<MusicDirectory> {
        val map = mutableMapOf<String, MusicDirectory>()

        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.AudioColumns.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.let { cur ->
            val columnIndexData = cur.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            while (cur.moveToNext()) {
                val absolutePath = cur.getString(columnIndexData)
                val folderName: String = File(absolutePath).parentFile?.name ?: ""

                if (map.containsKey(folderName)) {
                    with(map.getValue(folderName)) { count += 1 }
                } else {
                    val folderPath = absolutePath.substringBeforeLast(delimiter = "/")
                    map[folderName] = MusicDirectory(folderName, 1, folderPath)
                }

            }
            cur.close()
        }

        return map.values.sortedBy { it.folderName }
    }
}

data class MusicDirectory(
    val folderName: String,
    var count: Int,
    val path: String
)