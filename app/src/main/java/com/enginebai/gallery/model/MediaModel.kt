package com.enginebai.gallery.model

data class Media(
    val path: String,
    var name: String?,
    var album: String?,
    var size: Long?,
    var datetime: Long?,
    var duration: Long?,
    var width: Int?,
    var height: Int?
)

enum class MimeType(private val typeName: String) {
    ALL("all"),
    IMAGE("image"),
    VIDEO("video");

    override fun toString() = typeName
}