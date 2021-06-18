package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javavirys.mediaplayer.core.entity.PlayerStatus
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.domain.interactor.GetNextTrack
import com.javavirys.mediaplayer.domain.interactor.GetPreviousTrack
import com.javavirys.mediaplayer.domain.interactor.GetTrack
import com.javavirys.mediaplayer.domain.interactor.player.*

class TrackViewModel(
    private val getTrack: GetTrack,
    private val playTrack: PlayTrack,
    private val stopTrack: StopTrack,
    private val resumeTrack: StartTrack,
    private val pauseTrack: PauseTrack,
    private val getStatusTrack: GetStatusTrack,
    private val getNextTrack: GetNextTrack,
    private val getPreviousTrack: GetPreviousTrack,
) : BaseViewModel() {

    private val _playerLiveData = MutableLiveData<PlayerStatus>()

    private var track: Track? = null

    val playerLiveData: LiveData<PlayerStatus> = _playerLiveData

    fun play(trackId: Long): LiveData<PlayerStatus> {
        launch(
            backgroundCode = {
                track = getTrack.execute(trackId)
                playTrack.execute(track!!)
            },
            foregroundCode = {
                _playerLiveData.value = PlayerStatus.INITIALIZED(it)
                _playerLiveData.value = PlayerStatus.PLAYED(Unit)
            },
            catchCode = {
                it.printStackTrace()
            }
        )
        return playerLiveData
    }

    fun updateTimePosition() {
        subscribeOnFlow(
            backgroundCode = {
                getStatusTrack.execute(Unit)
            },
            foregroundCode = {
                _playerLiveData.value = it
            }
        )
    }

    fun resumeTrack() {
        launch(
            backgroundCode = {
                resumeTrack.execute(Unit)
            },
            foregroundCode = {
                _playerLiveData.value = PlayerStatus.PLAYED(Unit)
            }
        )
    }

    fun pauseTrack() {
        launch(
            backgroundCode = {
                pauseTrack.execute(Unit)
            },
            foregroundCode = {
                _playerLiveData.value = PlayerStatus.PAUSED(Unit)
            }
        )
    }

    fun stopTrack() {
        launch(
            backgroundCode = {
                stopTrack.execute(Unit)
            },
            foregroundCode = {
                _playerLiveData.value = PlayerStatus.PAUSED(Unit)
            }
        )
    }

    fun nextTrack() {
        track?.let {
            launch(
                backgroundCode = {
                    track = getNextTrack.execute(it)
                    playTrack.execute(track!!)
                }
            )
        }
    }

    fun previousTrack() {
        track?.let {
            launch(
                backgroundCode = {
                    track = getPreviousTrack.execute(it)
                    playTrack.execute(track!!)
                }
            )
        }
    }
}