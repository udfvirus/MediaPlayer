package com.javavirys.mediaplayer.di

import androidx.room.Room
import com.javavirys.mediaplayer.data.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "media-player"
        ).build()
    }
    single { get<AppDatabase>().getTrackDao() }
}