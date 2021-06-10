package com.javavirys.mediaplayer.domain.repository

import com.javavirys.mediaplayer.core.entity.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    suspend fun getTrack(position: Int): Track

    suspend fun getAllTracks(): Flow<Track>

    suspend fun getTrackCount(): Int

    suspend fun addTrack(track: Track)
}