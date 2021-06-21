package com.javavirys.mediaplayer.core.entity

sealed class PlayerStatus {
    data class NotReady(val unit: Unit) : PlayerStatus()
    data class Initialized(val trackInformation: TrackInformation) : PlayerStatus()
    data class Released(val unit: Unit) : PlayerStatus()
    data class Played(val trackInformation: TrackInformation) : PlayerStatus()
    data class Paused(val unit: Unit) : PlayerStatus()
}