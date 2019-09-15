package com.enginebai.gallery.ui

import androidx.lifecycle.ViewModel
import com.enginebai.gallery.model.AlbumRepo
import com.enginebai.gallery.model.AlbumSetting
import org.koin.core.KoinComponent
import org.koin.core.inject

class GalleryViewModel : ViewModel(), KoinComponent {
    var setting: AlbumSetting? = null

    private val albumRepo: AlbumRepo by inject()


}