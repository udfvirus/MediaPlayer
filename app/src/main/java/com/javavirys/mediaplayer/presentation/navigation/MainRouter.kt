package com.javavirys.mediaplayer.presentation.navigation

import androidx.navigation.findNavController
import com.javavirys.mediaplayer.ActivityProvider
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.presentation.screen.TrackListFragmentDirections

class MainRouter(private val activityProvider: ActivityProvider) {

    fun navigateToTrackScreen(track: Track) {
        val action = TrackListFragmentDirections.actionTrackListFragmentToTrackFragment(track.id)
        activityProvider.activeActivity
            ?.findNavController(R.id.fragmentContainer)
            ?.navigate(action)
    }
}