package com.javavirys.mediaplayer.di

import com.javavirys.mediaplayer.presentation.viewmodel.SplashViewModel
import com.javavirys.mediaplayer.presentation.viewmodel.TrackListViewModel
import com.javavirys.mediaplayer.presentation.viewmodel.TrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { TrackListViewModel(get(), get(), get()) }
    viewModel { TrackViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}