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

package com.javavirys.mediaplayer.util

import android.util.Log
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.util.extension.id
import com.javavirys.mediaplayer.util.extension.isPlayEnabled
import com.javavirys.mediaplayer.util.extension.isPlaying
import com.javavirys.mediaplayer.util.extension.isPrepared

object PlayerUtils {

    private const val LOG_TAG = "PlayerUtils"

    fun playMedia(
        musicServiceConnection: MusicServiceConnection,
        mediaItem: Track,
        pauseAllowed: Boolean = true
    ) {
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val transportControls = musicServiceConnection.transportControls

        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId == nowPlaying?.id) {
            musicServiceConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying ->
                        if (pauseAllowed) transportControls.pause() else Unit
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.w(
                            LOG_TAG,
                            "Playable item clicked but neither play nor pause are enabled!" +
                                    " (mediaId=${mediaItem.mediaId})"
                        )
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(mediaItem.id.toString(), null)
        }
    }

    fun playMediaId(musicServiceConnection: MusicServiceConnection, mediaId: String) {
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val transportControls = musicServiceConnection.transportControls

        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaId == nowPlaying?.id) {
            musicServiceConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.w(
                            LOG_TAG,
                            "Playable item clicked but neither play nor pause are enabled!" +
                                    " (mediaId=$mediaId)"
                        )
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(mediaId, null)
        }
    }

    fun playNextMedia(musicServiceConnection: MusicServiceConnection) {
        val transportControls = musicServiceConnection.transportControls
        transportControls.skipToNext()
    }

    fun playPreviousMedia(musicServiceConnection: MusicServiceConnection) {
        val transportControls = musicServiceConnection.transportControls
        transportControls.skipToPrevious()
    }

    fun updateTimePosition(musicServiceConnection: MusicServiceConnection, position: Long) {
        val transportControls = musicServiceConnection.transportControls
        transportControls.seekTo(position)
    }
}