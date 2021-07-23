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

package com.javavirys.mediaplayer

import android.app.Application
import com.javavirys.mediaplayer.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MediaPlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        startKoin {
            androidLogger()
            androidContext(this@MediaPlayerApplication)
            modules(
                listOf(
                    appModule,
                    mediaModule,
                    databaseModule,
                    repositoryModule,
                    interactorModule,
                    navigationModule,
                    viewModelModule
                )
            )
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}