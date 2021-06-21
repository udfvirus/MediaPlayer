package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.javavirys.mediaplayer.presentation.navigation.SplashRouter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val router: SplashRouter) : BaseViewModel() {

    fun navigateToMainScreen() {
        viewModelScope.launch {
            delay(SPLASH_DELAY)
            router.navigateToMainScreen()
        }
    }

    companion object {

        private const val SPLASH_DELAY = 3000L
    }
}