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