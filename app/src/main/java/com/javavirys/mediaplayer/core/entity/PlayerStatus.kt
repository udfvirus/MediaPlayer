package com.javavirys.mediaplayer.core.entity

sealed class PlayerStatus {
    data class INITIALIZED(val trackInformation: TrackInformation) : PlayerStatus()
    data class PLAYED(val unit: Unit) : PlayerStatus()
    data class PAUSED(val unit: Unit) : PlayerStatus()
    data class PROGRESS(val time: Int) : PlayerStatus()
}