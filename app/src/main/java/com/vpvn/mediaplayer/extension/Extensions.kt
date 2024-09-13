package com.vpvn.mediaplayer.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.encodeUTF8(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString()).replace("+", " ")
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

context(ViewModel)
fun <T> Flow<T>.stateInWhileSubscribed(initialValue: T): StateFlow<T> {
    return stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialValue,
    )
}