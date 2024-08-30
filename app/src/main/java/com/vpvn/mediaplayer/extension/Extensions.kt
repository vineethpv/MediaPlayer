package com.vpvn.mediaplayer.extension

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.encodeUTF8(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}