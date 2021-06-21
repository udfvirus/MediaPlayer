package com.javavirys.mediaplayer.domain.interactor

import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.domain.repository.TrackRepository

class GetNextTrack(
    private val trackRepository: TrackRepository
) : Interactor<Track, Track> {

    override suspend fun execute(param: Track): Track {
        val tracks = trackRepository.getTrackList()
        val currentIndex = tracks.indexOfFirst { it.id == param.id }

        return tracks[currentIndex + 1]
    }
}