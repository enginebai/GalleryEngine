package com.enginebai.gallery.di

import com.enginebai.gallery.ui.GalleryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}

val viewModelModule = module {
    viewModel { GalleryViewModel() }
}