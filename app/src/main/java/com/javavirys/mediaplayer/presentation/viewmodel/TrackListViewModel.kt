package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import com.javavirys.mediaplayer.core.entity.Result
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.entity.Worker
import com.javavirys.mediaplayer.data.worker.FileScannerWorker
import com.javavirys.mediaplayer.domain.interactor.GetAllTracks
import com.javavirys.mediaplayer.domain.interactor.RunAudioScanner
import com.javavirys.mediaplayer.presentation.navigation.MainRouter
import kotlinx.coroutines.launch

class TrackListViewModel(
    private val router: MainRouter,
    private val getAllTracks: GetAllTracks,
    private val runAudioScanner: RunAudioScanner,
) : BaseViewModel() {

    private val tracksLiveData = MutableLiveData<Result<Track>>()

    fun loadTracks(): LiveData<Result<Track>> {
        scanDirectories()

        subscribeOnFlow(
            backgroundCode = { getAllTracks.execute(Unit) },
            foregroundCode = { tracksLiveData.value = Result.Success(it) },
            catchCode = { tracksLiveData.value = Result.Error(it) }
        )

        return tracksLiveData
    }

    private fun scanDirectories() {
        viewModelScope.launch {
            val workRequest = OneTimeWorkRequestBuilder<FileScannerWorker>()
                .build()
            runAudioScanner.execute(Worker(workRequest))
        }
    }

    fun navigateToTrackScreen(track: Track) {
        router.navigateToTrackScreen(track)
    }
}