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

package com.javavirys.mediaplayer.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.presentation.viewmodel.SplashViewModel
import com.javavirys.mediaplayer.util.extension.findView
import com.javavirys.mediaplayer.util.extension.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val splashConstraintLayout by lazy { findView<ConstraintLayout>(R.id.splashConstraintLayout) }

    private val logoImageView by lazy { findView<ImageView>(R.id.logoImageView) }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            it.values.forEach { isGranted ->
                if (!isGranted) {
                    splashConstraintLayout.showSnackbar(
                        R.string.permission_denied,
                        Snackbar.LENGTH_INDEFINITE,
                        R.string.ok
                    ) {
                        navigateToMainScreenAndCheckPermissions()
                    }
                    return@registerForActivityResult
                }
            }
            navigateToMainScreen()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Glide.with(this).load(R.drawable.splash).into(logoImageView)
        navigateToMainScreenAndCheckPermissions()
    }

    private fun navigateToMainScreenAndCheckPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            navigateToMainScreen()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        } else {
            splashConstraintLayout.showSnackbar(
                R.string.permission_not_available,
                Snackbar.LENGTH_INDEFINITE,
                R.string.ok
            ) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun navigateToMainScreen() {
        val model: SplashViewModel by viewModel()
        model.navigateToMainScreen()
    }
}