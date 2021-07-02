/*
 * Copyright 2021 Vitaliy Sychov. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.javavirys.mediaplayer.presentation.navigation.SplashRouter
import com.javavirys.mediaplayer.util.MusicServiceConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val router: SplashRouter,
    private val musicServiceConnection: MusicServiceConnection
) : BaseViewModel() {

    fun navigateToMainScreen() {
        if (musicServiceConnection.isConnected.value == null) {
            viewModelScope.launch {
                delay(SPLASH_DELAY)
                router.navigateToMainScreen()
            }
        } else {
            router.navigateToMainScreen()
        }
    }

    companion object {

        private const val SPLASH_DELAY = 3000L
    }
}