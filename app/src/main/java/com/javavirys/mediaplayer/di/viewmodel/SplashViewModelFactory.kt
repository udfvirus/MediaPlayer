package com.javavirys.mediaplayer.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javavirys.mediaplayer.di.RouterFactory
import com.javavirys.mediaplayer.presentation.screen.SplashActivity
import com.javavirys.mediaplayer.presentation.viewmodel.SplashViewModel

class SplashViewModelFactory(private val activity: SplashActivity) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(RouterFactory.getSplashRouter(activity)) as T
    }
}