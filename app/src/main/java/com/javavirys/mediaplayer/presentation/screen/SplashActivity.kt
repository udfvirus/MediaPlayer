package com.javavirys.mediaplayer.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.di.viewmodel.SplashViewModelFactory
import com.javavirys.mediaplayer.presentation.viewmodel.SplashViewModel
import com.javavirys.mediaplayer.util.extension.findView
import com.javavirys.mediaplayer.util.extension.showSnackbar

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val model: SplashViewModel by viewModels { SplashViewModelFactory(this) }

    private val splashConstraintLayout by lazy { findView<ConstraintLayout>(R.id.splashConstraintLayout) }

    private val logoImageView by lazy { findView<ImageView>(R.id.logoImageView) }

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                model.navigateToMainScreen()
            } else {
                splashConstraintLayout.showSnackbar(
                    R.string.permission_denied,
                    Snackbar.LENGTH_SHORT,
                    R.string.ok
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Glide.with(this).load(R.drawable.splash).into(logoImageView)
        navigateToMainScreen()
    }

    private fun navigateToMainScreen() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            model.navigateToMainScreen()
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
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            splashConstraintLayout.showSnackbar(
                R.string.permission_not_available,
                Snackbar.LENGTH_LONG,
                R.string.ok
            ) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}