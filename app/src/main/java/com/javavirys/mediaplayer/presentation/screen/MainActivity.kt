package com.javavirys.mediaplayer.presentation.screen

import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.javavirys.mediaplayer.R

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(ActivityCompat.requireViewById(this, R.id.toolbar))
    }
}