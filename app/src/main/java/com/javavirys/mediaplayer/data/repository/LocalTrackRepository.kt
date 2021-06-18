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

    override suspend fun getTrackCount() = trackDao.getTracksCount()

    override suspend fun addTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override suspend fun getNextTrack(track: Track) =
        TrackMapper().toTrack(trackDao.getNextTrack(track.id) ?: throw TrackNotFoundException())

    override suspend fun getPreviousTrack(track: Track) =
        TrackMapper().toTrack(trackDao.getPreviousTrack(track.id) ?: throw TrackNotFoundException())
}