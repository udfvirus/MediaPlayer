package com.javavirys.mediaplayer.di

import com.javavirys.mediaplayer.presentation.viewmodel.SplashViewModel
import com.javavirys.mediaplayer.presentation.viewmodel.TrackListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TrackListViewModel(get(), get()) }
    viewModel { SplashViewModel(get()) }
}