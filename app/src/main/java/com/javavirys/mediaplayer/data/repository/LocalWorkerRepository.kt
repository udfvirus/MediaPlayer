package com.javavirys.mediaplayer.data.repository

import android.content.Context
import androidx.work.WorkManager
import com.javavirys.mediaplayer.core.entity.Worker
import com.javavirys.mediaplayer.domain.repository.WorkerRepository

class LocalWorkerRepository(private val context: Context) : WorkerRepository {

    override suspend fun setWorker(worker: Worker) {
        WorkManager.getInstance(context)
            .enqueue(worker.workRequest)
    }
}