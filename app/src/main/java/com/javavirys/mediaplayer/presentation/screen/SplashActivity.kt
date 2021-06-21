package com.javavirys.mediaplayer.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
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

class SplashActivity : BaseActivity<SplashViewModel>(R.layout.activity_splash) {

    override val model: SplashViewModel by viewModel()

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
                        navigateToMainScreen()
                    }
                    return@registerForActivityResult
                }
            }
            model.navigateToMainScreen()
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
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
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
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
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
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }
}