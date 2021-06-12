package com.javavirys.mediaplayer.domain.interactor

import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.domain.repository.TrackRepository

class GetNextTrack(
    private val trackRepository: TrackRepository
) : Interactor<Track, Track> {

    override suspend fun execute(param: Track) = trackRepository.getNextTrack(param)
}