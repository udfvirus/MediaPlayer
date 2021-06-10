package com.javavirys.mediaplayer.domain.interactor.additional

import com.javavirys.mediaplayer.domain.interactor.Interactor

abstract class InteractorWithoutParam<R> : Interactor<Unit, R> {

    override suspend fun execute(param: Unit) = execute()

    protected abstract suspend fun execute(): R
}