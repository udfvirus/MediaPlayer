/*
 * Copyright 2021 Vitaliy Sychov. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        println("test1: doWork")
//        async {
//            println("test1: count = " + trackDao.getTracksCount())
//            trackDao.getAllTracks().collect { track: TrackDbo? ->
//                println("test1: getAllTracks.track = $track")
//                if (track != null) {
//                    println("test1: getAllTracks.id = " + track?.id)
//                    println("test1: getAllTracks.name = " + track?.name)
//                }
//            }
//        }
//
//        try {
//            println("test1: track == ")
//            println("test1: track path = " + Environment.getExternalStorageDirectory().path)
//            scanDirectories(Environment.getExternalStorageDirectory().path)
//        } catch (exception: Exception) {
//            exception.printStackTrace()
//            println("test1: doWork exception = $exception")
//        }
//        println("test1: success")
//        Result.success()
        return@withContext try {
//           async {
//                println("test1: count = " + trackDao.getTracksCount())
//                trackDao.getAllTracks().collect {
//                    println("test1: track.id = " + it.id)
//                    println("test1: track.name = " + it.name)
//                }
//            }
            scanDirectories(Environment.getExternalStorageDirectory().path)
            Result.success()
        } catch (error: Throwable) {
            Result.failure()
        }
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
                println("test: processFile.audio = ${file.nameWithoutExtension}")
                try {
                    val id = trackDao.insert(TrackDbo(0, file.path, file.nameWithoutExtension))
                    println("test: processFile.id = $id")
                    println("test: processFile.count = " + trackDao.getTracksCount())
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    println("test: processFile exception = $exception")
                }
            }
        }
    }
}