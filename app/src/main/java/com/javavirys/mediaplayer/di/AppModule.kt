package com.javavirys.mediaplayer.di

import com.javavirys.mediaplayer.ActivityProvider
import org.koin.dsl.module

val appModule = module {
    single { ActivityProvider(get()) }
}