package com.javavirys.mediaplayer.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javavirys.mediaplayer.data.repository.MockTrackRepository
import com.javavirys.mediaplayer.presentation.viewmodel.TrackListViewModel

class TrackListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackListViewModel(MockTrackRepository()) as T
    }
}