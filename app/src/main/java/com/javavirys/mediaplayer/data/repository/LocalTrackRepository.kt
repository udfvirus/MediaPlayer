package com.javavirys.mediaplayer.data.repository

import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.data.database.dao.TrackDao
import com.javavirys.mediaplayer.data.mapper.TrackMapper
import com.javavirys.mediaplayer.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalTrackRepository(
    private val trackDao: TrackDao
) : TrackRepository {

    override suspend fun getTrack(position: Int): Track {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTracks(): Flow<Track> {
        return trackDao.getAllTracks()
            .map { TrackMapper().toTrack(it) }
    }

    override suspend fun getTrackCount() = trackDao.getTracksCount()

    override suspend fun addTrack(track: Track) {
        TODO("Not yet implemented")
    }
}