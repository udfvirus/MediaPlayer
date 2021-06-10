package com.javavirys.mediaplayer.domain.repository

import com.javavirys.mediaplayer.core.entity.Worker

interface WorkerRepository {

    suspend fun setWorker(worker: Worker)
}