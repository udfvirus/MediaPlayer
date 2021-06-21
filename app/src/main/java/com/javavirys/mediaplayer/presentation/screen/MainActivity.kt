package com.javavirys.mediaplayer.presentation.screen

import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel>(R.layout.activity_main) {

    override val model: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(ActivityCompat.requireViewById(this, R.id.toolbar))
    }
}