package com.javavirys.mediaplayer.data.repository

import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.ResourceTrack
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.domain.repository.TrackRepository

class MockTrackRepository : TrackRepository {

    private val tracks = mutableListOf(
        ResourceTrack("Sound 1", R.raw.track_one)
    )

    override suspend fun getTrack(position: Int) = tracks[position]

    override suspend fun getAllTracks(): List<Track> = tracks

    override suspend fun getTrackCount() = tracks.size

    override suspend fun addTrack(track: Track) {
        TODO("Not yet implemented")
    }
}