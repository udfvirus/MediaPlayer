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

package com.javavirys.mediaplayer.data.repository

import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.exception.TrackNotFoundException
import com.javavirys.mediaplayer.data.database.dao.TrackDao
import com.javavirys.mediaplayer.data.mapper.TrackMapper
import com.javavirys.mediaplayer.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class LocalTrackRepository(
    private val trackDao: TrackDao
) : TrackRepository {

    override suspend fun getTrack(id: Long): Track {
        return TrackMapper().toTrack(trackDao.getTrackById(id))
    }

    override suspend fun getAllTracks(): Flow<Track> {
        return flow {
            trackDao.getAllTracks().collect {
                it.map { item ->
                    emit(TrackMapper().toTrack(item))
                }
            }
        }
    }

    override suspend fun getTrackList(): List<Track> {
        return trackDao.getTrackList()
            .map { TrackMapper().toTrack(it) }
    }

    override suspend fun getTrackCount() = trackDao.getTracksCount()

    override suspend fun addTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override suspend fun getNextTrack(track: Track) =
        TrackMapper().toTrack(trackDao.getNextTrack(track.id) ?: throw TrackNotFoundException())

    override suspend fun getPreviousTrack(track: Track) =
        TrackMapper().toTrack(trackDao.getPreviousTrack(track.id) ?: throw TrackNotFoundException())
}