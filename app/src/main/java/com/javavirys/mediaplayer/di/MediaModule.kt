package com.javavirys.mediaplayer.di

import android.media.MediaPlayer
import com.javavirys.mediaplayer.data.service.MediaService
import org.koin.dsl.module

val mediaModule = module {
    single { MediaPlayer() }
    single { MediaService(get()) }
}