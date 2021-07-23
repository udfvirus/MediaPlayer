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
package com.javavirys.mediaplayer.data.service

import android.support.v4.media.MediaMetadataCompat
import com.javavirys.mediaplayer.data.database.AppDatabase
import com.javavirys.mediaplayer.data.mapper.TrackMapper
import com.javavirys.mediaplayer.data.worker.FileScannerWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class MediaStorage(database: AppDatabase) {

    private val trackDao = database.getTrackDao()

    private var state = State.STATE_CREATE
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                synchronized(onReadyListener) {
                    field = value
                    onReadyListener(value)
                }
            } else {
                field = value
            }
        }

    private var onReadyListener: ((State) -> Unit) = {}

    suspend fun load() {
        state = State.STATE_INITIALIZING
        val trackDboList = trackDao.getTrackList()
        if (trackDboList.isEmpty()) {
            FileScannerWorker(trackDao).doWork()
            trackDao.getTrackList()
        } else {
            launchScannerUseGlobalScope()
            trackDboList
        }.map { TrackMapper().toMediaMetadataCompat(it) }
        state = State.STATE_INITIALIZED
    }

    private fun launchScannerUseGlobalScope() {
        GlobalScope.launch(Dispatchers.IO) { FileScannerWorker(trackDao).doWork() }
    }

    fun whenReady(performAction: (State) -> Unit): Boolean = when (state) {
        State.STATE_CREATE, State.STATE_INITIALIZING -> {
            onReadyListener = performAction
            false
        }
        else -> {
            performAction(state)
            true
        }
    }

    fun getTrackList(): List<MediaMetadataCompat> {
        return runBlocking {
            trackDao.getTrackList()
                .filter { File(it.filePath).exists() }
                .map { TrackMapper().toMediaMetadataCompat(it) }
        }
    }

    enum class State {
        STATE_CREATE,
        STATE_INITIALIZING,
        STATE_INITIALIZED,
        STATE_ERROR
    }
}