package com.javavirys.mediaplayer.presentation.screen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.PlayerStatus
import com.javavirys.mediaplayer.presentation.viewmodel.TrackViewModel
import com.javavirys.mediaplayer.util.extension.findView
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackFragment : BaseFragment<TrackViewModel>(R.layout.fragment_track) {

    private val args: TrackFragmentArgs by navArgs()

    override val model: TrackViewModel by viewModel()

    private lateinit var visualizer: LottieAnimationView

    private lateinit var playImageView: ImageView

    private lateinit var nextImageView: ImageView

    private lateinit var prevImageView: ImageView

    private lateinit var progress: LinearProgressIndicator

    private val toolbar by lazy { requireActivity().findView<Toolbar>(R.id.toolbar) }

    private var trackId = 0L

    private var audioSessionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackId = savedInstanceState?.getLong(TRACK_ID_KEY, args.trackId) ?: args.trackId
        model.setTrackId(trackId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visualizer = view.findView(R.id.visualizer)

        progress = view.findView(R.id.progress)

        playImageView = view.findView(R.id.playImageView)

        nextImageView = view.findView(R.id.nextImageView)

        prevImageView = view.findView(R.id.prevImageView)

        playImageView.setOnClickListener {
            if (model.playerLiveData.value is PlayerStatus.Paused) {
                model.resumeTrack()
            } else {
                model.pauseTrack()
            }
        }

        nextImageView.setOnClickListener {
            if (model.playerLiveData.value !is PlayerStatus.NotReady) {
                model.nextTrack()
            }
        }

        prevImageView.setOnClickListener {
            if (model.playerLiveData.value !is PlayerStatus.NotReady) {
                model.previousTrack()
            }
        }

        model.playerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is PlayerStatus.Initialized -> {
                }
                is PlayerStatus.Played -> {
                    visualizer.resumeAnimation()
                    trackId = it.trackInformation.track.id
                    progress.max = it.trackInformation.duration
                    toolbar.title = it.trackInformation.track.name

                    Glide.with(this)
                        .load(R.drawable.ic_baseline_stop_circle_24)
                        .into(playImageView)
                }
                is PlayerStatus.Paused -> {
                    visualizer.pauseAnimation()
                    Glide.with(this)
                        .load(R.drawable.ic_baseline_play_circle_24)
                        .into(playImageView)
                }
                is PlayerStatus.Released -> {
                    Glide.with(this)
                        .load(R.drawable.ic_baseline_play_circle_24)
                        .into(playImageView)
                }
            }
        }

        model.trackInformationLiveData.observe(viewLifecycleOwner) {
            progress.progress = it
        }

        model.play()
    }

    override fun onStop() {
        super.onStop()
        model.pauseTrack()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(TRACK_ID_KEY, trackId)
    }

    override fun onDestroyView() {
        model.stopTrack()
        super.onDestroyView()
    }

    companion object {
        private const val TRACK_ID_KEY = "TRACK_ID_KEY"
    }
}