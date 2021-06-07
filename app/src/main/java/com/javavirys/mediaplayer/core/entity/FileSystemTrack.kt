package com.javavirys.mediaplayer.core.entity

data class FileSystemTrack(
    override val name: String,
    val path: String
) : Track