package com.enginebai.gallery.model

import android.content.Context
import android.provider.MediaStore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.File

interface AlbumRepo {
    fun fetchAlbums(setting: AlbumSetting? = null): Completable
    fun getAlbums(setting: AlbumSetting? = null): BehaviorSubject<List<AlbumItem>>
    fun getAlbumItem(name: String, setting: AlbumSetting? = null): Observable<AlbumItem>
    fun getAlbumItemSync(name: String, settings: AlbumSetting? = null): AlbumItem?
}

class AlbumRepoImpl(private val context: Context) : AlbumRepo {

    private val albumSubject = BehaviorSubject.create<List<AlbumItem>>()
    private val albumItemMapping = mutableMapOf<String, AlbumItem>()

    override fun fetchAlbums(setting: AlbumSetting?): Completable {
        return Completable.fromAction {
            fetchAlbumSync(setting)
        }
    }

    private fun fetchAlbumSync(setting: AlbumSetting?) {
        albumItemMapping.clear()
        val contentUri = MediaStore.Files.getContentUri("external")
        val selection =
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR " +
                    "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) AND " +
                    "${MediaStore.MediaColumns.SIZE} > 0"
        val selectionArgs =
            arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )
        val projections =
            arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Video.Media.DURATION
            )

        val sortBy = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"
        val cursor =
            context.contentResolver.query(contentUri, projections, selection, selectionArgs, sortBy)
        if (true == cursor?.moveToFirst()) {
            val pathCol = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
            val bucketNameCol =
                cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val nameCol = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
            val dateCol = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED)
            val mimeType = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
            val sizeCol = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)
            val durationCol = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
            val widthCol = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH)
            val heightCol = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT)

            do {
                val path = cursor.getString(pathCol)
                val bucketName = cursor.getString(bucketNameCol)
                val name = cursor.getString(nameCol)
                val dateTime = cursor.getLong(dateCol)
                val type = cursor.getString(mimeType)
                val size = cursor.getLong(sizeCol)
                val duration = cursor.getLong(durationCol)
                val width = cursor.getInt(widthCol)
                val height = cursor.getInt(heightCol)

                if (path.isNullOrEmpty() || type.isNullOrEmpty())
                    continue

                val file = File(path)
                if (!file.exists() || !file.isFile)
                    continue

                if (MimeType.IMAGE == setting?.mimeType &&
                    !type.startsWith(MimeType.IMAGE.toString())
                )
                    continue
                if (MimeType.VIDEO == setting?.mimeType &&
                    !type.startsWith(MimeType.VIDEO.toString())
                )
                    continue

                if (type.startsWith(MimeType.IMAGE.toString())) {
                    if (null != setting?.imageMaxSize) {
                        if (size > setting.imageMaxSize!!) {
                            continue
                        }
                    }
                }

                if (type.startsWith(MimeType.VIDEO.toString())) {
                    if (null != setting?.videoMinSecond && duration < setting.videoMinSecond!!.times(1000)) {
                        continue
                    }

                    if (null != setting?.videoMaxSecond && duration > setting.videoMaxSecond!!.times(1000)) {
                        continue
                    }
                }

                val media = Media(
                    path,
                    name,
                    bucketName,
                    size,
                    dateTime,
                    duration,
                    width,
                    height
                )

                // 初始化所有媒體的相簿
                if (isEmpty()) {
                    addAlbumItem(ALL_MEDIA_ALBUM_NAME, "", path)
                }
                // 把目前媒體放到所有媒體的相簿
                addMediaToAlbum(ALL_MEDIA_ALBUM_NAME, media)

                // 把目前媒體放到對應的相簿
                val folder = file.parentFile?.absolutePath ?: ""
                addAlbumItem(bucketName, folder, path)
                addMediaToAlbum(bucketName, media)

            } while (cursor.moveToNext())
        }
        cursor?.close()
        albumSubject.onNext(albumItemMapping.values.toList())
    }

    override fun getAlbums(setting: AlbumSetting?): BehaviorSubject<List<AlbumItem>> {
        if (isEmpty()) {
            fetchAlbums(setting)
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
        return albumSubject
    }

    override fun getAlbumItem(name: String, setting: AlbumSetting?): Observable<AlbumItem> {
        if (isEmpty()) {
            fetchAlbums(setting)
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
        return albumSubject.map {
            albumItemMapping[name]
        }
    }

    override fun getAlbumItemSync(name: String, settings: AlbumSetting?): AlbumItem? {
        if (isEmpty())
            fetchAlbumSync(settings)
        return albumItemMapping[name]
    }

    private fun addAlbumItem(name: String, folder: String, coverImagePath: String) {
        albumItemMapping[name] ?: run {
            albumItemMapping[name] =
                AlbumItem(name, folder, coverImagePath)
        }
    }

    private fun addMediaToAlbum(albumName: String, media: Media) {
        albumItemMapping[albumName]?.mediaList?.add(media)
    }

    private fun isEmpty() = albumItemMapping.keys.isEmpty()
}