package com.javavirys.mediaplayer.data.worker

import android.os.Environment
import com.javavirys.mediaplayer.data.database.dao.TrackDao
import com.javavirys.mediaplayer.data.database.entity.TrackDbo
import java.io.File

class TestFileScannerWorker(private val trackDao: TrackDao) {

    suspend fun doWork() {
        scanDirectories(Environment.getExternalStorageDirectory().path)
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
                try {
                    trackDao.insert(TrackDbo(0, file.path, file.nameWithoutExtension))
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
    }
}