package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    protected fun <R> subscribeOnFlow(
        backgroundCode: suspend () -> Flow<R>,
        foregroundCode: (result: R) -> Unit,
        catchCode: (throwable: Throwable) -> Unit = {},
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
        catchCode: (throwable: Throwable) -> Unit = {},
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
}