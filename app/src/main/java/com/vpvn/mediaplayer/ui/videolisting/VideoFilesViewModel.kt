package com.vpvn.mediaplayer.ui.videolisting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class VideoFilesViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val directoryName: String = savedStateHandle.get<String>("directoryPath")!!
    private val _directoryNameLiveData = MutableLiveData(directoryName)
    val directoryNameLiveData: LiveData<String>
        get() = _directoryNameLiveData
}