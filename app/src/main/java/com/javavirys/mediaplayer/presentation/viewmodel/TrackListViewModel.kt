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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.OneTimeWorkRequestBuilder
import com.javavirys.mediaplayer.core.entity.FileSystemTrack
import com.javavirys.mediaplayer.core.entity.Result
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.entity.Worker
import com.javavirys.mediaplayer.data.service.MediaPlaybackService.Companion.ROOT_ID
import com.javavirys.mediaplayer.data.worker.FileScannerWorker
import com.javavirys.mediaplayer.domain.interactor.GetTrackCount
import com.javavirys.mediaplayer.domain.interactor.RunAudioScanner
import com.javavirys.mediaplayer.presentation.navigation.MainRouter
import com.javavirys.mediaplayer.util.MusicServiceConnection
import com.javavirys.mediaplayer.util.PlayerUtils

class TrackListViewModel(
    private val router: MainRouter,
    private val getTrackCount: GetTrackCount,
    private val runAudioScanner: RunAudioScanner,
    private val musicServiceConnection: MusicServiceConnection
) : BaseViewModel() {

    val scannerStatusLiveData = MutableLiveData<Result<Unit>>()

    private val tracksLiveData = MutableLiveData<Result<Track>>()

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {

        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {
            children.map { child ->
                val subtitle = child.description.subtitle ?: ""
                tracksLiveData.value =
                    Result.Success(
                        FileSystemTrack(
                            child.mediaId!!.toLong(),
                            child.description.title.toString(),
                            child.mediaId!!,
                            child.mediaId!!
                        )
                    )
            }
        }
    }

    fun loadTracks(): LiveData<Result<Track>> {
        scanDirectories()

        return tracksLiveData
    }

    private fun scanDirectories() {
        scannerStatusLiveData.value = Result.Progress()
        subscribeMusicServiceConnectionIfTracksExists()

        launch(
            backgroundCode = {
                val workRequest = OneTimeWorkRequestBuilder<FileScannerWorker>()
                    .build()
                runAudioScanner.execute(Worker(workRequest))
            },
            foregroundCode = {
                scannerStatusLiveData.value = Result.Success(Unit)
                musicServiceConnection.subscribe(ROOT_ID, subscriptionCallback)
            }
        )
    }

    private fun subscribeMusicServiceConnectionIfTracksExists() {
        launch(
            backgroundCode = { getTrackCount.execute(Unit) },
            foregroundCode = {
                if (it > 0) {
                    scannerStatusLiveData.value = Result.Success(Unit)
                    musicServiceConnection.subscribe(ROOT_ID, subscriptionCallback)
                }
            }
        )
    }

    fun navigateToTrackScreen(track: Track) {
        playMedia(track, false)
        router.navigateToTrackScreen(track)
    }

    private fun playMedia(mediaItem: Track, pauseAllowed: Boolean = true) {
        PlayerUtils.playMedia(musicServiceConnection, mediaItem, pauseAllowed)
    }
}