package com.javavirys.mediaplayer.core.entity

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
    data class Progress(val progress: Int = 0) : Result<Int>()
}