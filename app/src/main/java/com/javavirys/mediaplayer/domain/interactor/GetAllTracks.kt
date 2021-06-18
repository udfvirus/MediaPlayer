package com.javavirys.mediaplayer.domain.interactor

import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.domain.interactor.additional.InteractorWithoutParam
import com.javavirys.mediaplayer.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow

class GetAllTracks(
    private val trackRepository: TrackRepository
) : InteractorWithoutParam<Flow<Track>>() {

    override suspend fun execute() = trackRepository.getAllTracks()
}