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

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import com.javavirys.mediaplayer.core.entity.FileSystemTrack
import com.javavirys.mediaplayer.core.entity.Result
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.data.service.MediaPlaybackService.Companion.ROOT_ID
import com.javavirys.mediaplayer.presentation.navigation.MainRouter
import com.javavirys.mediaplayer.util.MusicServiceConnection
import com.javavirys.mediaplayer.util.PlayerUtils

class TrackListViewModel(
    private val router: MainRouter,
    musicServiceConnection: MusicServiceConnection
) : BaseViewModel() {

    val scannerStatusLiveData = MutableLiveData<Result<Unit>>()

    val tracksLiveData = MutableLiveData<Result<Track>>()

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {

        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
            scannerStatusLiveData.value = Result.Success(Unit)
            children.map { child ->
                tracksLiveData.value =
                    Result.Success(
                        FileSystemTrack(
                            child.mediaId!!.toLong(),
                            child.description.title.toString(),
                            child.mediaId!!
                        )
                    )
            }
        }
    }

    private val musicServiceConnection = musicServiceConnection.also {
        it.subscribe(ROOT_ID, subscriptionCallback)
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(ROOT_ID, subscriptionCallback)
    }

    fun navigateToTrackScreen(track: Track) {
        playMedia(track, false)
        router.navigateToTrackScreen(track)
    }

    private fun playMedia(mediaItem: Track, pauseAllowed: Boolean = true) {
        PlayerUtils.playMedia(musicServiceConnection, mediaItem, pauseAllowed)
    }
}