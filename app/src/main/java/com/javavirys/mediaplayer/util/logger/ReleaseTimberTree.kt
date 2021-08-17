/*
 * Copyright 2021 Vitaliy Sychov
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

package com.javavirys.mediaplayer.util.logger

import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.javavirys.mediaplayer.core.exception.LogException
import timber.log.Timber

class ReleaseTimberTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        when (priority) {
            Log.INFO -> {
                Firebase.analytics.setUserProperty(FIREBASE_LOG_NAME, message)
                Firebase.analytics.logEvent(FIREBASE_LOG_NAME) {
                    param(
                        FIREBASE_LOG_MESSAGE_KEY,
                        message
                    ) // Message can be up to 100 characters long.
                }
            }

            Log.ERROR -> {
                val crashlytics = FirebaseCrashlytics.getInstance()
                val throwable = t ?: LogException(message)
                crashlytics.log("priority: $priority")
                crashlytics.log("tag: $tag")
                crashlytics.log("message: $message")
                crashlytics.recordException(throwable)
            }
        }
    }

    
    companion object {

        private const val FIREBASE_LOG_NAME = "log_name"

        private const val FIREBASE_LOG_MESSAGE_KEY = "log_message"
    }
}