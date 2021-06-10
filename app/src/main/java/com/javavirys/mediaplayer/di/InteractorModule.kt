package com.javavirys.mediaplayer.di

import com.javavirys.mediaplayer.domain.interactor.GetAllTracks
import com.javavirys.mediaplayer.domain.interactor.RunAudioScanner
import org.koin.dsl.module

val interactorModule = module {
    single { GetAllTracks(get()) }
    single { RunAudioScanner(get()) }
}