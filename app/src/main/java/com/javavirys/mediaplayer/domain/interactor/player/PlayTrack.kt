package com.javavirys.mediaplayer.domain.interactor.player

import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.entity.TrackInformation
import com.javavirys.mediaplayer.data.service.MediaService
import com.javavirys.mediaplayer.domain.interactor.Interactor

class PlayTrack(
    private val mediaService: MediaService
) : Interactor<Track, TrackInformation> {

    override suspend fun execute(param: Track) = mediaService.play(param)
}