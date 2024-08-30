package com.vpvn.mediaplayer.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vpvn.mediaplayer.NavRouteConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val mediaItemUriState: StateFlow<String> = savedStateHandle.getStateFlow(NavRouteConstants.MEDIA_ITEM_URI, "")

}