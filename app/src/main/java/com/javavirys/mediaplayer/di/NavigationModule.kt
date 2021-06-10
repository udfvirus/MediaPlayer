package com.javavirys.mediaplayer.di

import com.javavirys.mediaplayer.presentation.navigation.MainRouter
import com.javavirys.mediaplayer.presentation.navigation.SplashRouter
import org.koin.dsl.module

val navigationModule = module {
    factory { SplashRouter(get()) }
    factory { MainRouter(get()) }
}