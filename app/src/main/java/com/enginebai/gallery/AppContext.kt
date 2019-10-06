package com.enginebai.gallery

import android.app.Application
import com.enginebai.gallery.di.appModule
import com.enginebai.gallery.di.repoModule
import com.enginebai.gallery.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppContext : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppContext)
            modules(listOf(
                appModule,
                viewModelModule,
                repoModule
            ))
        }
    }
}