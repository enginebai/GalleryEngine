package com.enginebai.gallery.model

import java.io.Serializable

const val ALL_MEDIA_ALBUM_NAME = "ALL_MEDIA_ALBUM_NAME"

data class AlbumItem(
    val name: String,
    val folder: String,
    val coverImagePath: String
) {
    val mediaList = mutableListOf<Media>()
}

class AlbumSetting : Serializable {
    var mimeType = MimeType.ALL
    var multipleSelection: Boolean = false
    var maxSelection = 10
    var imageMaxSize: Long? = null
    var videoMaxSecond: Int? = null
    var videoMinSecond: Int? = null
}