package com.javavirys.mediaplayer.domain.interactor.player

import com.javavirys.mediaplayer.data.service.MediaService
import com.javavirys.mediaplayer.domain.interactor.additional.InteractorWithoutParam
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetStatusTrack(
    private val mediaService: MediaService
) : InteractorWithoutParam<Flow<Int>>() {

    override suspend fun execute(): Flow<Int> = flow {
        while (mediaService.isPlaying()) {
            emit(mediaService.getPosition())
            delay(DELAY_FOR_GET_POSITION)
        }
    }

    companion object {
        private const val DELAY_FOR_GET_POSITION = 800L
    }
}