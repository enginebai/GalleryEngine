package com.enginebai.gallery.model

const val ALL_MEDIA_ALBUM_NAME = "ALL_MEDIA_ALBUM_NAME"

data class AlbumItem(
    val name: String,
    val folder: String,
    val coverImagePath: String
) {
    val mediaList = mutableListOf<Media>()
}

class AlbumSetting {
    var mimeType = MimeType.ALL
    var multipleSelection: Boolean = false
    var maxSelection = 1
    var imageMaxSize: Long? = null
    var videoMaxSecond: Int? = null
    var videoMinSecond: Int? = null
}