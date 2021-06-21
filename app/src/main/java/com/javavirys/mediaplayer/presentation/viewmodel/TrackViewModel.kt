package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javavirys.mediaplayer.core.entity.PlayerStatus
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.entity.TrackInformation
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

    private val _trackInformationLiveData = MutableLiveData<Int>()

    private var trackInformation: TrackInformation? = null

    private var trackId = 0L

    val playerLiveData: LiveData<PlayerStatus> = _playerLiveData

    val trackInformationLiveData: LiveData<Int> = _trackInformationLiveData

    fun setTrackId(trackId: Long) {
        this.trackId = trackId
    }

    fun play() {
        launch(
            backgroundCode = {
                val track = getTrack.execute(trackId)
                playTrack.execute(track)
            },
            foregroundCode = {
                changeStatusToPlayed(it)
                updateTimePosition()
            }
        )
    }

    private fun updateTimePosition() {
        subscribeOnFlow(
            backgroundCode = {
                getStatusTrack.execute(Unit)
            },
            foregroundCode = {
                _trackInformationLiveData.value = it
            }
        )
    }

    fun resumeTrack() {
        launch(
            backgroundCode = {
                resumeTrack.execute(Unit)
            },
            foregroundCode = {
                trackInformation?.let {
                    _playerLiveData.value = PlayerStatus.Played(it)
                }
            }
        )
    }

    fun pauseTrack() {
        launch(
            backgroundCode = {
                pauseTrack.execute(Unit)
            },
            foregroundCode = {
                _playerLiveData.value = PlayerStatus.Paused(Unit)
            }
        )
    }

    fun stopTrack() {
        launch(
            backgroundCode = {
                stopTrack.execute(Unit)
            },
            foregroundCode = {
                _playerLiveData.value = PlayerStatus.Paused(Unit)
            }
        )
    }

    fun nextTrack() {
        gotoTrack {
            getNextTrack.execute(it)
        }
    }

    fun previousTrack() {
        gotoTrack {
            getPreviousTrack.execute(it)
        }
    }

    private fun gotoTrack(getTrackFunction: suspend (Track) -> Track) {
        trackInformation?.let {
            _playerLiveData.value = PlayerStatus.NotReady(Unit)
            launch(
                backgroundCode = {
                    val track = getTrackFunction(it.track)
                    playTrack.execute(track)
                },
                foregroundCode = {
                    trackInformation = it
                    _playerLiveData.value = PlayerStatus.Played(it)
                    updateTimePosition()
                }
            )
        }
    }

    private fun changeStatusToPlayed(trackInformation: TrackInformation) {
        this.trackInformation = trackInformation
        _playerLiveData.value = PlayerStatus.Initialized(trackInformation)
        _playerLiveData.value = PlayerStatus.Played(trackInformation)
    }

    override fun onException(throwable: Throwable) {
        if (throwable !is ArrayIndexOutOfBoundsException) {
            super.onException(throwable)
        }
    }

    override fun onCleared() {
        _playerLiveData.value = PlayerStatus.Released(Unit)
        super.onCleared()
    }
}