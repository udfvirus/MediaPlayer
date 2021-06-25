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

package com.javavirys.mediaplayer.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.navigation.ActivityNavigator
import com.javavirys.mediaplayer.presentation.screen.MainActivity

class SplashRouter(private val context: Context) {

    private val activityNavigator = ActivityNavigator(context)

    fun navigateToMainScreen() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activityNavigator.navigate(
            activityNavigator.createDestination().setIntent(intent),
            null,
            null,
            null
        )
    }
}