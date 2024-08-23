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
        val map = mutableMapOf<String, Int>()
        val folderNameList = mutableListOf<MediaDirectory>()
        val cursor: Cursor?
        var columnIndexData = 0

        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
        cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.let { cur ->
            columnIndexData = cur.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA)
            while (cur.moveToNext()) {
                val absolutePathOfImage = cur.getString(columnIndexData)
                val fileName: String = File(absolutePathOfImage).parentFile?.name ?: ""

                if (map.containsKey(fileName)) {
                    map[fileName] = map.getValue(fileName) + 1
                } else {
                    map[fileName] = 1
                }

            }
            cur.close()
        }

        map.forEach { (key, value) ->
            folderNameList.add(MediaDirectory(key, value))
        }
        return folderNameList.sortedBy { it.folderName }
    }
}