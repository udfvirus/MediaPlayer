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

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.javavirys.mediaplayer.data.service.MediaStorage
import com.javavirys.mediaplayer.data.service.PersistentStorage.Companion.MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS
import com.javavirys.mediaplayer.util.extension.id
import timber.log.Timber

class MediaPlaybackPreparer(
    private val mediaStorage: MediaStorage,
    private val preparePlaylist: (
        tracks: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat,
        playWhenReady: Boolean,
        playbackStartPositionMs: Long
    ) -> Unit
) : MediaSessionConnector.PlaybackPreparer {

    override fun onCommand(
        player: Player,
        controlDispatcher: ControlDispatcher,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSupportedPrepareActions(): Long =
        PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

    override fun onPrepare(playWhenReady: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onPrepareFromMediaId(
        mediaId: String,
        playWhenReady: Boolean,
        extras: Bundle?
    ) {
        Timber.d("onPrepareFromMediaId mediaId=$mediaId playWhenReady=$playWhenReady")
        // MediaId
        val tracks = mediaStorage.getTrackList()

        val itemToPlay = tracks.find { item ->
            item.id.toString() == mediaId
        }
        if (itemToPlay == null) {
            // TODO: Notify caller of the error.
        } else {
            val playbackStartPositionMs = extras?.getLong(
                MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS,
                C.TIME_UNSET
            ) ?: C.TIME_UNSET
            preparePlaylist(
                tracks,
                itemToPlay,
                playWhenReady,
                playbackStartPositionMs
            )
        }
    }

    override fun onPrepareFromSearch(
        query: String,
        playWhenReady: Boolean,
        extras: Bundle?
    ) {
        TODO("Not yet implemented")
    }

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) {
        TODO("Not yet implemented")
    }
}