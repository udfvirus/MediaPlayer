/*
 * Copyright 2021 Vitaliy Sychov
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

package com.javavirys.mediaplayer.data.service.listener

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import timber.log.Timber

class MediaPlayerEventListener(
    private val onShowNotificationForPlayer: () -> Unit,
    private val onHideNotificationForPlayer: () -> Unit,
    private val onStopForegroundService: (removeNotification: Boolean) -> Unit,
    private val saveRecentSongToStorage: () -> Unit
) : Player.EventListener {

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING,
            Player.STATE_READY -> {
                onShowNotificationForPlayer()
                if (playbackState == Player.STATE_READY) {
                    saveRecentSongToStorage()
                    if (!playWhenReady) {
                        onStopForegroundService(false)
                    }
                }
            }
            else -> onHideNotificationForPlayer()
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        error.printStackTrace()
        Timber.e("onPlayerError.error: $error")
    }
}