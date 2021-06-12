package com.javavirys.mediaplayer.domain.interactor

import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.domain.repository.TrackRepository

class GetTrack(
    private val trackRepository: TrackRepository
) : Interactor<Long, Track> {

    override suspend fun execute(param: Long) = trackRepository.getTrack(param)
}