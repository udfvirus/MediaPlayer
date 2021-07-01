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

import android.os.Environment
import com.javavirys.mediaplayer.data.database.dao.TrackDao
import com.javavirys.mediaplayer.data.database.entity.TrackDbo
import java.io.File

class FileScannerWorker(private val trackDao: TrackDao) {

    suspend fun doWork() {
        scanDirectories(Environment.getExternalStorageDirectory().path) // scan sdcard
        scanDirectories(STORAGE_DIR_PATH) // scan removable memory card
    }

    private suspend fun scanDirectories(path: String) {
        try {
            val file = File(path)
            file.list()?.let {
                it.forEach { item ->
                    val filePath = "$path/$item"
                    processFilePath(filePath)
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
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

    companion object {

        private const val STORAGE_DIR_PATH = "/storage"
    }
}