package com.javavirys.mediaplayer.di

import com.javavirys.mediaplayer.domain.interactor.*
import com.javavirys.mediaplayer.domain.interactor.player.*
import org.koin.dsl.module

val interactorModule = module {
    single { GetAllTracks(get()) }
    single { RunAudioScanner(get()) }
    single { GetTrack(get()) }
    single { PlayTrack(get()) }
    single { StopTrack(get()) }
    single { PauseTrack(get()) }
    single { StartTrack(get()) }
    single { GetStatusTrack(get()) }
    single { GetNextTrack(get()) }
    single { GetPreviousTrack(get()) }
}