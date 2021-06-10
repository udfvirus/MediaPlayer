package com.javavirys.mediaplayer.data.worker

import android.content.Context
import android.os.Environment
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.javavirys.mediaplayer.data.database.entity.TrackDbo
import com.javavirys.mediaplayer.di.DatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FileScannerWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val trackDao = DatabaseFactory.getDatabaseInstance(applicationContext)
        .getTrackDao()

    override suspend fun doWork() = withContext(Dispatchers.Default) {
        scanDirectories(Environment.getExternalStorageDirectory().path)
        Result.success()
    }

    private suspend fun scanDirectories(path: String) {
        val file = File(path)
        file.list()?.let {
            it.forEach { item ->
                val filePath = "$path/$item"
                processFilePath(filePath)
            }
        }
    }

    private suspend fun processFilePath(filePath: String) {
        val file = File(filePath)
        if (file.isDirectory) {
            scanDirectories(filePath)
        } else {
            processFile(file)
        }
    }

    private suspend fun processFile(file: File) {
        when (file.extension) {
            "mp3" -> {
                trackDao.insert(TrackDbo(0, file.path, file.nameWithoutExtension))
            }
        }
    }
}