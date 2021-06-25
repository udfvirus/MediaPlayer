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

package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    private val exceptionLiveData = MutableLiveData<Throwable>()

    fun getExceptions(): LiveData<Throwable> = exceptionLiveData

    protected fun <R> subscribeOnFlow(
        backgroundCode: suspend () -> Flow<R>,
        foregroundCode: (result: R) -> Unit,
        catchCode: (throwable: Throwable) -> Unit = ::onException,
        backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO,
        foregroundDispatcher: CoroutineDispatcher = Dispatchers.Main
    ) {
        viewModelScope.launch(backgroundDispatcher) {
            try {
                backgroundCode.invoke()
                    .collect {
                        withContext(foregroundDispatcher) {
                            foregroundCode.invoke(it)
                        }
                    }
            } catch (throwable: Throwable) {
                withContext(foregroundDispatcher) {
                    catchCode.invoke(throwable)
                }
            }
        }
    }

    protected fun <R> launch(
        backgroundCode: suspend () -> R,
        foregroundCode: (result: R) -> Unit = {},
        catchCode: (throwable: Throwable) -> Unit = ::onException,
        backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO,
        foregroundDispatcher: CoroutineDispatcher = Dispatchers.Main
    ) {
        viewModelScope.launch(backgroundDispatcher) {
            try {
                val result = backgroundCode.invoke()
                withContext(foregroundDispatcher) {
                    foregroundCode.invoke(result)
                }
            } catch (throwable: Throwable) {
                withContext(foregroundDispatcher) {
                    catchCode.invoke(throwable)
                }
            }
        }
    }

    protected open fun onException(throwable: Throwable) {
        throwable.printStackTrace()
        exceptionLiveData.value = throwable
    }
}