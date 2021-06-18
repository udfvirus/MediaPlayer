package com.javavirys.mediaplayer.presentation.screen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.chibde.visualizer.CircleBarVisualizer
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.PlayerStatus
import com.javavirys.mediaplayer.presentation.viewmodel.TrackViewModel
import com.javavirys.mediaplayer.util.extension.findView
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackFragment : Fragment(R.layout.fragment_track) {

    private val args: TrackFragmentArgs by navArgs()

    private val model: TrackViewModel by viewModel()

    private lateinit var visualizer: CircleBarVisualizer

    private lateinit var playImageView: ImageView

    private lateinit var nextImageView: ImageView

    private lateinit var prevImageView: ImageView

    private lateinit var progress: LinearProgressIndicator

    private val toolbar by lazy { requireActivity().findView<Toolbar>(R.id.toolbar) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visualizer = view.findView(R.id.visualizer)

        progress = view.findView(R.id.progress)

        playImageView = view.findView(R.id.playImageView)

        nextImageView = view.findView(R.id.nextImageView)

        prevImageView = view.findView(R.id.prevImageView)

        playImageView.setOnClickListener {
            if (model.playerLiveData.value is PlayerStatus.PAUSED) {
                model.resumeTrack()
            } else {
                model.pauseTrack()
            }
        }

        nextImageView.setOnClickListener {
            model.nextTrack()
        }

        prevImageView.setOnClickListener {
            model.previousTrack()
        }

        model.playerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is PlayerStatus.INITIALIZED -> {
                    visualizer.setPlayer(it.trackInformation.audioSessionId)
                    progress.max = it.trackInformation.duration
                    toolbar.title = it.trackInformation.track.name
                }
                is PlayerStatus.PLAYED -> {
                    Glide.with(this)
                        .load(R.drawable.ic_baseline_stop_circle_24).into(playImageView)
                    model.updateTimePosition()
                }
                is PlayerStatus.PAUSED -> Glide.with(this)
                    .load(R.drawable.ic_baseline_play_circle_24).into(playImageView)
                is PlayerStatus.PROGRESS -> {
                    progress.progress = it.time
                }
            }
        }

        model.play(args.trackId)
    }

    override fun onStop() {
        super.onStop()
        model.pauseTrack()
    }

    override fun onDestroyView() {
        model.stopTrack()
        super.onDestroyView()
    }
}