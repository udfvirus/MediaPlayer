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
import com.javavirys.mediaplayer.core.entity.Result
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.exception.TrackNotFoundException
import com.javavirys.mediaplayer.data.service.MediaPlaybackService.Companion.ROOT_ID
import com.javavirys.mediaplayer.domain.interactor.DeleteTrackInteractor
import com.javavirys.mediaplayer.presentation.navigation.MainRouter
import com.javavirys.mediaplayer.util.MusicServiceConnection
import com.javavirys.mediaplayer.util.PlayerUtils

class TrackListViewModel(
    private val router: MainRouter,
    private val deleteTrackInteractor: DeleteTrackInteractor,
    musicServiceConnection: MusicServiceConnection,
) : BaseViewModel() {

    val scannerStatusLiveData = MutableLiveData<Result<Unit>>()

    val tracksLiveData = MutableLiveData<Result<Track>>()

    val selectedModeTrackLiveData = MutableLiveData<Boolean>()

    val selectedTrackListLiveData = MutableLiveData<List<Track>>()

    val updateTrackLiveData = MutableLiveData<Track>()

    val removeTrackLiveData = MutableLiveData<Track>()

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {

        override fun onChildrenLoaded(
            parentId: String,
            children: List<MediaBrowserCompat.MediaItem>
        ) {

            scannerStatusLiveData.value = Result.Success(Unit)
            if (children.isEmpty()) {
                tracksLiveData.value = Result.Error(TrackNotFoundException())
            }
            children.map { child ->
                tracksLiveData.value =
                    Result.Success(
                        Track(
                            child.mediaId!!.toLong(),
                            child.description.title.toString(),
                            child.description.mediaUri.toString()
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

    fun onTrackClicked(track: Track, list: List<Track>) {
        if (selectedModeTrackLiveData.value == true) {
            track.selected = !track.selected
            updateTrackLiveData.value = track
            val selectedTracks = list.filter { it.selected }

            selectedTrackListLiveData.value = selectedTracks
            if (selectedTracks.isEmpty()) {
                selectedModeTrackLiveData.value = false
            }
        } else {
            navigateToTrackScreen(track)
        }
    }

    fun navigateToTrackScreen(track: Track) {
        selectedTrackListLiveData.value?.let {
            it.forEach { item -> item.selected = false }
            selectedModeTrackLiveData.value = false
        }
        playMedia(track, false)
        router.navigateToTrackScreen(track)
    }

    fun navigateToTrackInformationScreen() {
        selectedTrackListLiveData.value?.let {
            it.forEach { item -> item.selected = false }
            selectedModeTrackLiveData.value = false
            router.navigateToTrackInformationScreen(it)
        }
    }

    private fun playMedia(mediaItem: Track, pauseAllowed: Boolean = true) {
        PlayerUtils.playMedia(musicServiceConnection, mediaItem, pauseAllowed)
    }

    fun setSelectedMode(track: Track) {
        if (selectedModeTrackLiveData.value != true) {
            selectedModeTrackLiveData.value = true
            updateTrackLiveData.value = track.also { it.selected = true }
            selectedTrackListLiveData.value = listOf(track)
        }
    }

    fun deleteSelectedTracks() {
        selectedTrackListLiveData.value?.let {
            deleteTracks(it)
        }
    }

    private fun deleteTracks(list: List<Track>) {
        selectedModeTrackLiveData.value = false
        launch(
            backgroundCode = {
                list.forEach {
                    deleteTrackInteractor.execute(it)
                    removeTrackLiveData.postValue(it)
                }
            }
        )
    }
}