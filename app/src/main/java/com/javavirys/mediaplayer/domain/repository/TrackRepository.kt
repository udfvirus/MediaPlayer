package com.javavirys.mediaplayer.domain.repository

import com.javavirys.mediaplayer.core.entity.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    suspend fun getTrack(id: Long): Track

    suspend fun getAllTracks(): Flow<Track>

    suspend fun getTrackCount(): Int

    suspend fun addTrack(track: Track)

    suspend fun getNextTrack(track: Track): Track

    suspend fun getPreviousTrack(track: Track): Track
}