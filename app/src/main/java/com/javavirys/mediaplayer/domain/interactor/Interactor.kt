package com.javavirys.mediaplayer.domain.interactor

interface Interactor<P, R> {

    suspend fun execute(param: P): R
}