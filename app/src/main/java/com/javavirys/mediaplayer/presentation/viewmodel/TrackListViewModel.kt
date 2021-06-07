package com.javavirys.mediaplayer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.domain.repository.TrackRepository
import kotlinx.coroutines.launch

class TrackListViewModel(
    private val trackRepository: TrackRepository
) : ViewModel() {

    private val tracksLiveData = MutableLiveData<List<Track>>()

    fun loadTracks(): LiveData<List<Track>> {
        viewModelScope.launch {
            tracksLiveData.postValue(trackRepository.getAllTracks())
        }

        return tracksLiveData
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