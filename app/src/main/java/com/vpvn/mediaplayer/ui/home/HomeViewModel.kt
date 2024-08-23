package com.vpvn.mediaplayer.ui.home

import android.content.Context
import android.database.Cursor
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
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _mediaDirectoriesLiveData = MutableLiveData(emptyList<MediaDirectory>())
    val mediaDirectoriesLiveData: LiveData<List<MediaDirectory>>
        get() = _mediaDirectoriesLiveData

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _mediaDirectoriesLiveData.postValue(getMediaFolders())
            }
        }
    }

    private fun getMediaFolders(): List<MediaDirectory> {
        val map = mutableMapOf<String, MediaDirectory>()
        val cursor: Cursor?
        var columnIndexData: Int

        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
        cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.let { cur ->
            columnIndexData = cur.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA)
            while (cur.moveToNext()) {
                val absolutePath = cur.getString(columnIndexData)
                val folderName: String = File(absolutePath).parentFile?.name ?: ""

                if (map.containsKey(folderName)) {
                    with(map.getValue(folderName)) { videoCount += 1 }
                } else {
                    val folderPath = absolutePath.substringBeforeLast(delimiter = "/")
                    map[folderName] = MediaDirectory(folderName, 1, folderPath)
                }

            }
            cur.close()
        }

        return map.values.sortedBy { it.folderName }
    }
}