package com.javavirys.mediaplayer.data.repository

import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.ResourceTrack
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.exception.InvalidIdException
import com.javavirys.mediaplayer.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockTrackRepository : TrackRepository {

    private val tracks = mutableListOf(
        ResourceTrack(1, "Sound 1", R.raw.track_one)
    )

    override suspend fun getTrack(id: Long) =
        tracks.find { it.id == id } ?: throw InvalidIdException()

    override suspend fun getAllTracks(): Flow<Track> = flow {
        tracks.forEach {
            emit(it)
        }
    }

    override suspend fun getTrackCount() = tracks.size

    override suspend fun addTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override suspend fun getNextTrack(track: Track): Track {
        TODO("Not yet implemented")
    }

    override suspend fun getPreviousTrack(track: Track): Track {
        TODO("Not yet implemented")
    }
}