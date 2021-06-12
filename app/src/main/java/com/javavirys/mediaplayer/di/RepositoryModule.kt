package com.javavirys.mediaplayer.di

import com.javavirys.mediaplayer.data.repository.LocalTrackRepository
import com.javavirys.mediaplayer.data.repository.LocalWorkerRepository
import com.javavirys.mediaplayer.domain.repository.TrackRepository
import com.javavirys.mediaplayer.domain.repository.WorkerRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> { LocalTrackRepository(get()) }
    single<WorkerRepository> { LocalWorkerRepository(get(), get()) }
}