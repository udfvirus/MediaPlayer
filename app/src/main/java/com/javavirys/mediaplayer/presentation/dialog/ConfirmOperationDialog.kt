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

package com.javavirys.mediaplayer.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.javavirys.mediaplayer.R

class ConfirmOperationDialog(
    private val title: String,
    private val message: String,
    private val onConfirmed: () -> Unit = {},
    private val onCanceled: () -> Unit = {}
) : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    onConfirmed()
                }.setNegativeButton(R.string.cancel) { _, _ ->
                    onCanceled
                }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}