package com.javavirys.mediaplayer.domain.repository

import com.javavirys.mediaplayer.core.entity.Track

interface TrackRepository {

    suspend fun getTrack(position: Int): Track

    suspend fun getAllTracks(): List<Track>

    suspend fun getTrackCount(): Int

    suspend fun addTrack(track: Track)
}