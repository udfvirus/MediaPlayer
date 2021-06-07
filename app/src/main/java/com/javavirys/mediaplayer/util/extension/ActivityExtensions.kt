package com.javavirys.mediaplayer.util.extension

import android.view.View
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity

fun <T : View> FragmentActivity.findView(@IdRes idRes: Int): T =
    ActivityCompat.requireViewById(this, idRes)