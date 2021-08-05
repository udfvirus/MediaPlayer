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

package com.javavirys.mediaplayer.presentation.mapper

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.javavirys.mediaplayer.core.entity.PlayingMetadata
import com.javavirys.mediaplayer.core.entity.PlayingStatus
import com.javavirys.mediaplayer.util.TimeUtils
import com.javavirys.mediaplayer.util.extension.*

class PlayingMetadataMapper {

    fun toPlayingMetadata(
        mediaMetadata: MediaMetadataCompat,
        playbackState: PlaybackStateCompat
    ): PlayingMetadata {
        return PlayingMetadata(
            mediaMetadata.id!!,
            mediaMetadata.albumArtUri,
            mediaMetadata.title?.trim(),
            mediaMetadata.displaySubtitle?.trim(),
            TimeUtils.timestampToMSS(mediaMetadata.duration),
            mediaMetadata.duration,
            transformPlaybackStateCompatToPlayingStatus(playbackState)
        )
    }

    fun transformPlaybackStateCompatToPlayingStatus(playbackState: PlaybackStateCompat) =
        if (playbackState.isPlaying) {
            PlayingStatus.STATE_PLAYING
        } else {
            PlayingStatus.STATE_PAUSED
        }
}