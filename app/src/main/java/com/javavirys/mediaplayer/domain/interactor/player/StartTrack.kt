package com.javavirys.mediaplayer.domain.interactor.player

import com.javavirys.mediaplayer.data.service.MediaService
import com.javavirys.mediaplayer.domain.interactor.Interactor

class StartTrack(
    private val mediaService: MediaService
) : Interactor<Unit, Unit> {

    override suspend fun execute(param: Unit) = mediaService.start()
}