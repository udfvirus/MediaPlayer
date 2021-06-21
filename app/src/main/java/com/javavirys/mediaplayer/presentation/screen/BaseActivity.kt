package com.javavirys.mediaplayer.presentation.screen

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.javavirys.mediaplayer.presentation.viewmodel.BaseViewModel
import com.javavirys.mediaplayer.util.extension.findView

abstract class BaseActivity<M : BaseViewModel> : AppCompatActivity {

    protected abstract val model: M

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.getExceptions().observe(this, ::showException)
    }

    protected open fun showException(throwable: Throwable) =
        Snackbar.make(findView(android.R.id.content), throwable.toString(), Snackbar.LENGTH_LONG)
            .show()
}