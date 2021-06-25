/*
 * Copyright 2021 Vitaliy Sychov. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.javavirys.mediaplayer.core.entity.PlayingMetadata
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initClickListeners()
        initObservers(view)
    }

    private fun initViews(view: View) {
        visualizer = view.findView(R.id.visualizer)
        progress = view.findView(R.id.progress)
        progress.progress = 0
        playImageView = view.findView(R.id.playImageView)
        nextImageView = view.findView(R.id.nextImageView)
        prevImageView = view.findView(R.id.prevImageView)
    }

    private fun initClickListeners() {
        playImageView.setOnClickListener {
            model.mediaMetadata.value?.let { model.playMediaId(it.id) }
        }

        nextImageView.setOnClickListener {
            model.nextTrack()
        }

        prevImageView.setOnClickListener {
            model.previousTrack()
        }
    }

    private fun initObservers(view: View) {
        model.mediaMetadata.observe(viewLifecycleOwner) { mediaItem -> updateUI(view, mediaItem) }

        model.mediaButtonRes.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it.second)
                .into(playImageView)

            updateVisualizerState(it.first)
        }

        model.mediaPosition.observe(viewLifecycleOwner) { pos ->
            progress.progress = pos.toInt()
        }
    }

    private fun updateVisualizerState(play: Boolean) = if (play) {
        visualizer.resumeAnimation()
    } else {
        visualizer.pauseAnimation()
    }

    private fun updateUI(view: View, metadata: PlayingMetadata) {
        toolbar.title = metadata.title
        progress.max = metadata.durationMsec.toInt()
    }
}