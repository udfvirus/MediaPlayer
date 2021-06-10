package com.javavirys.mediaplayer.domain.interactor

import com.javavirys.mediaplayer.core.entity.Worker
import com.javavirys.mediaplayer.domain.interactor.additional.InteractorWithoutResult
import com.javavirys.mediaplayer.domain.repository.WorkerRepository

class RunAudioScanner(
    private val workerRepository: WorkerRepository
) : InteractorWithoutResult<Worker>() {

    override suspend fun exec(param: Worker) {
        workerRepository.setWorker(param)
    }
}