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

package com.javavirys.mediaplayer.di

import android.content.Context
import androidx.room.Room
import com.javavirys.mediaplayer.data.database.AppDatabase

object DatabaseFactory {

    private const val DATABASE_NAME = "mediaplayer"

    private var appDatabase: AppDatabase? = null

    fun getDatabaseInstance(applicationContext: Context): AppDatabase {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
        }

        return appDatabase!!
    }
}