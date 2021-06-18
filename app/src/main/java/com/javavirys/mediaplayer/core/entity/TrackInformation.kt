package com.javavirys.mediaplayer.core.entity

data class TrackInformation(
    val audioSessionId: Int,
    val duration: Int,
    val track: Track
)