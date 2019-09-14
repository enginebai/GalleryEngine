package com.enginebai.gallery.model

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