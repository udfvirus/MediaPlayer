package com.javavirys.mediaplayer.data.repository

import android.content.Context
import com.javavirys.mediaplayer.core.entity.Worker
import com.javavirys.mediaplayer.data.database.dao.TrackDao
import com.javavirys.mediaplayer.data.worker.TestFileScannerWorker
import com.javavirys.mediaplayer.domain.repository.WorkerRepository

class LocalWorkerRepository(private val context: Context, private val trackDao: TrackDao) :
    WorkerRepository {

    override suspend fun setWorker(worker: Worker) {
        TestFileScannerWorker(trackDao).doWork()
//        WorkManager.getInstance(context)
//            .enqueueUniqueWork("worker",
//                ExistingWorkPolicy.KEEP,
//                worker.workRequest)
    }
}