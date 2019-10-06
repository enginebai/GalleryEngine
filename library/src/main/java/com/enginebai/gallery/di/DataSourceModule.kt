package com.enginebai.gallery.di

import com.enginebai.gallery.model.AlbumRepo
import com.enginebai.gallery.model.AlbumRepoImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single<AlbumRepo> {
        AlbumRepoImpl(
            androidContext()
        )
    }
}