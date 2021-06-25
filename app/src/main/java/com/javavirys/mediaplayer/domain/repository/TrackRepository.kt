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

package com.javavirys.mediaplayer.domain.repository

import com.javavirys.mediaplayer.core.entity.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    suspend fun getTrack(id: Long): Track

    suspend fun getAllTracks(): Flow<Track>

    suspend fun getTrackList(): List<Track>

    suspend fun getTrackCount(): Int

    suspend fun addTrack(track: Track)

    suspend fun getNextTrack(track: Track): Track

    suspend fun getPreviousTrack(track: Track): Track
}