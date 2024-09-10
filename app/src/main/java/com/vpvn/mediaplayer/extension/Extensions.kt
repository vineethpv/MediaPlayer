package com.vpvn.mediaplayer.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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