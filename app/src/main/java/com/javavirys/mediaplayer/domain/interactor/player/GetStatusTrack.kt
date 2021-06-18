package com.javavirys.mediaplayer.domain.interactor.player

import com.javavirys.mediaplayer.core.entity.PlayerStatus
import com.javavirys.mediaplayer.data.service.MediaService
import com.javavirys.mediaplayer.domain.interactor.additional.InteractorWithoutParam
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetStatusTrack(
    private val mediaService: MediaService
) : InteractorWithoutParam<Flow<PlayerStatus>>() {

    override suspend fun execute(): Flow<PlayerStatus> = flow {
        while (mediaService.isPlaying()) {
            emit(PlayerStatus.PROGRESS(mediaService.getPosition()))
            delay(800)
        }
        emit(PlayerStatus.PAUSED(Unit))
    }
}