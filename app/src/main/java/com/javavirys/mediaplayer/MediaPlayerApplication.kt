package com.javavirys.mediaplayer

import android.app.Application
import com.javavirys.mediaplayer.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MediaPlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MediaPlayerApplication)
            modules(
                listOf(
                    appModule,
                    mediaModule,
                    databaseModule,
                    repositoryModule,
                    interactorModule,
                    navigationModule,
                    viewModelModule
                )
            )
        }
    }
}