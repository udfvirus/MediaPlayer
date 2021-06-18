package com.javavirys.mediaplayer.data.service

import android.media.MediaPlayer
import com.javavirys.mediaplayer.core.entity.FileSystemTrack
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.entity.TrackInformation

class MediaService(private val mediaPlayer: MediaPlayer) {

    fun play(track: Track): TrackInformation {
        if (track is FileSystemTrack) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(track.path)
        }
        mediaPlayer.prepare()
        mediaPlayer.start()

        return TrackInformation(mediaPlayer.audioSessionId, mediaPlayer.duration, track)
    }

    fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    fun start() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun getPosition() = mediaPlayer.currentPosition

    fun isPlaying() = mediaPlayer.isPlaying
}