package com.javavirys.mediaplayer.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javavirys.mediaplayer.data.repository.LocalTrackRepository
import com.javavirys.mediaplayer.presentation.viewmodel.TrackListViewModel

class TrackListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackListViewModel(LocalTrackRepository()) as T
    }
}