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

class MediaStorage(database: AppDatabase) {

    private val trackDao = database.getTrackDao()

    private var onReadyListener: ((List<MediaMetadataCompat>?) -> Unit) = {}

    private var tracks: List<MediaMetadataCompat>? = null
        set(value) {
            synchronized(onReadyListener) {
                field = value
                onReadyListener(value)
            }
        }

    suspend fun load() {
        val trackDboList = trackDao.getTrackList()
        tracks = if (trackDboList.isEmpty()) {
            FileScannerWorker(trackDao).doWork()
            trackDao.getTrackList()
        } else {
            launchScannerUseGlobalScope()
            trackDboList
        }.map { TrackMapper().toMediaMetadataCompat(it) }
    }

    private fun launchScannerUseGlobalScope() {
        GlobalScope.launch(Dispatchers.IO) { FileScannerWorker(trackDao).doWork() }
    }

    fun whenReady(performAction: (List<MediaMetadataCompat>?) -> Unit): Boolean =
        if (tracks.isNullOrEmpty()) {
            onReadyListener = performAction
            false
        } else {
            performAction(tracks)
            true
        }
}