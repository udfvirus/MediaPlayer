package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.core.entity.Worker
import com.javavirys.mediaplayer.data.worker.FileScannerWorker
import com.javavirys.mediaplayer.domain.interactor.GetAllTracks
import com.javavirys.mediaplayer.domain.interactor.RunAudioScanner
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TrackListViewModel(
    private val getAllTracks: GetAllTracks,
    private val runAudioScanner: RunAudioScanner,
) : ViewModel() {

    private val tracksLiveData = MutableLiveData<Track>()

    fun loadTracks(): LiveData<Track> {
        scanDirectories()
        viewModelScope.launch {
            getAllTracks.execute(Unit).collect {
                tracksLiveData.postValue(it)
            }
        }

        return tracksLiveData
    }

    private fun scanDirectories() {
        viewModelScope.launch {
            val workRequest = OneTimeWorkRequestBuilder<FileScannerWorker>()
                .build()
            runAudioScanner.execute(Worker(workRequest))
        }
    }

    fun deleteTrack(track: Track) {
        TODO("Not yet implemented")
    }

    fun navigateToTrackScreen(track: Track) {
        TODO("Not yet implemented")
    }

    fun navigateToAddTrackScreen() {
        TODO("Not yet implemented")
    }
}