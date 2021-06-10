package com.javavirys.mediaplayer.domain.interactor.additional

import com.javavirys.mediaplayer.domain.interactor.Interactor

abstract class InteractorWithoutResult<P> : Interactor<P, Unit> {

    override suspend fun execute(param: P) = exec(param)

    protected abstract suspend fun exec(param: P)
}