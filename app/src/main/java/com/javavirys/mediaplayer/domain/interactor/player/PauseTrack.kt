package com.javavirys.mediaplayer.domain.interactor.player

import com.javavirys.mediaplayer.data.service.MediaService
import com.javavirys.mediaplayer.domain.interactor.additional.InteractorWithoutParam

class PauseTrack(
    private val mediaService: MediaService
) : InteractorWithoutParam<Unit>() {

    override suspend fun execute() {
        mediaService.pause()
    }
}