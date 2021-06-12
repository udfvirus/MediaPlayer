package com.javavirys.mediaplayer.core.exception

abstract class BaseException : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)
}