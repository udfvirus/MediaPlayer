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

import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.PlayingMetadata
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.data.service.MediaPlaybackService.Companion.RECENT_ID
import com.javavirys.mediaplayer.util.MusicServiceConnection
import com.javavirys.mediaplayer.util.MusicServiceConnection.Companion.EMPTY_PLAYBACK_STATE
import com.javavirys.mediaplayer.util.MusicServiceConnection.Companion.NOTHING_PLAYING
import com.javavirys.mediaplayer.util.PlayerUtils
import com.javavirys.mediaplayer.util.TimeUtils
import com.javavirys.mediaplayer.util.extension.*
import timber.log.Timber

class TrackViewModel(
    musicServiceConnection: MusicServiceConnection
) : BaseViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var updatePosition = true

    val mediaMetadata = MutableLiveData<PlayingMetadata>()
    val mediaButtonRes = MutableLiveData<Pair<Boolean, Int>>()

    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        playbackState = it ?: EMPTY_PLAYBACK_STATE
        val metadata = musicServiceConnection.nowPlaying.value ?: NOTHING_PLAYING
        updateState(playbackState, metadata)
    }

    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        updateState(playbackState, it)
    }

    private var playbackState: PlaybackStateCompat = EMPTY_PLAYBACK_STATE

    private val recentSubscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {

        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
            if (children.isEmpty()) return

            children.map { child ->
                val track = Track(
                    child.mediaId!!.toLong(),
                    child.description.title.toString(),
                    child.description.mediaUri.toString()
                )
                Timber.d("recentSubscriptionCallback.onChildrenLoaded.track = $track")
                if (mediaMetadata.value?.id == child.mediaId) return
                mediaMetadata.value = PlayingMetadata(
                    child.mediaId!!,
                    child.description.iconUri,
                    child.description.title.toString(),
                    null,
                    null,
                    0
                )
            }
        }
    }

    private val musicServiceConnection = musicServiceConnection.also {
        it.subscribe(RECENT_ID, recentSubscriptionCallback)
        it.playbackState.observeForever(playbackStateObserver)
        it.nowPlaying.observeForever(mediaMetadataObserver)
        checkPlaybackPosition()
    }

    val mediaPosition = MutableLiveData<Long>().apply {
        postValue(0L)
    }

    fun nextTrack() {
        PlayerUtils.playNextMedia(musicServiceConnection)
    }

    fun previousTrack() {
        PlayerUtils.playPreviousMedia(musicServiceConnection)
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.playbackState.removeObserver(playbackStateObserver)
        musicServiceConnection.nowPlaying.removeObserver(mediaMetadataObserver)
        updatePosition = false
        musicServiceConnection.unsubscribe(RECENT_ID, recentSubscriptionCallback)
    }

    private fun checkPlaybackPosition(): Boolean = handler.postDelayed({
        val currPosition = playbackState.currentPlayBackPosition
        if (mediaPosition.value != currPosition) {
            mediaPosition.postValue(currPosition)
        }
        if (updatePosition) {
            checkPlaybackPosition()
        }
    }, POSITION_UPDATE_INTERVAL_MILLIS)

    private fun updateState(
        playbackState: PlaybackStateCompat,
        mediaMetadata: MediaMetadataCompat
    ) {
        if (mediaMetadata.duration != 0L && mediaMetadata.id != null) {
            val nowPlayingMetadata = PlayingMetadata(
                mediaMetadata.id!!,
                mediaMetadata.albumArtUri,
                mediaMetadata.title?.trim(),
                mediaMetadata.displaySubtitle?.trim(),
                TimeUtils.timestampToMSS(mediaMetadata.duration),
                mediaMetadata.duration
            )
            this.mediaMetadata.postValue(nowPlayingMetadata)
        }

        val resourceId = when (playbackState.isPlaying) {
            true -> R.drawable.ic_pause_black_24dp
            else -> R.drawable.ic_play_arrow_black_24dp
        }
        mediaButtonRes.postValue(Pair(playbackState.isPlaying, resourceId))
    }

    fun playMediaId(mediaId: String) {
        PlayerUtils.playMediaId(musicServiceConnection, mediaId)
    }

    fun updatePosition(position: Int) {
        PlayerUtils.updateTimePosition(musicServiceConnection, position.toLong())
    }

    companion object {
        private const val POSITION_UPDATE_INTERVAL_MILLIS = 100L
    }
}