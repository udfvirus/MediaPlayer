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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.PlayingMetadata
import com.javavirys.mediaplayer.core.entity.Result
import com.javavirys.mediaplayer.core.entity.Track
import com.javavirys.mediaplayer.presentation.adapter.TrackAdapter
import com.javavirys.mediaplayer.presentation.dialog.ConfirmOperationDialog
import com.javavirys.mediaplayer.presentation.viewmodel.MainSharedViewModel
import com.javavirys.mediaplayer.presentation.viewmodel.TrackListViewModel
import com.javavirys.mediaplayer.presentation.viewmodel.TrackViewModel
import com.javavirys.mediaplayer.util.extension.findView
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackListFragment : BaseFragment<TrackListViewModel>(R.layout.fragment_track_list) {

    override val model: TrackListViewModel by viewModel()

    private val trackViewModel: TrackViewModel by viewModel()

    private val adapter by lazy {
        TrackAdapter(
            onItemClick = { item, list ->
                model.onTrackClicked(item, list)
            },
            onItemLongClick = { model.setSelectedMode(it) }
        )
    }

    private lateinit var trackRecyclerView: RecyclerView

    private lateinit var progressLayout: ConstraintLayout

    private lateinit var noFilesFoundLayout: View

    private lateinit var playingLayout: CardView

    private lateinit var coverImageView: ImageView

    private lateinit var playImageView: ImageView

    private lateinit var nextImageView: ImageView

    private lateinit var nameTextView: TextView

    private lateinit var singerTextView: TextView

    private val rotateAnimation = RotateAnimation(
        0f,
        360f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    ).also {
        it.repeatCount = Animation.INFINITE
        it.duration = 3000
        it.interpolator = LinearInterpolator()
    }

    private val mainSharedViewModel by lazy {
        ViewModelProvider(requireActivity())[MainSharedViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLayout = view.findView(R.id.progressLayout)
        noFilesFoundLayout = view.findView(R.id.noFilesFoundLayout)
        initToolbar()
        initRecyclerView(view)
        initPlayingLayout(view)
    }

    private fun initToolbar() {
        val toolbar = requireActivity().findView<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.track_list_title)
    }

    private fun initRecyclerView(view: View) {
        trackRecyclerView = view.findView(R.id.trackRecyclerView)
        (trackRecyclerView.itemAnimator as SimpleItemAnimator).changeDuration = 0
        trackRecyclerView.adapter = adapter

        model.tracksLiveData.observe(viewLifecycleOwner) {
            if (it is Result.Success) {
                changePlayingStatusIfNeed(it.data)
                adapter.addItem(it.data)
            } else {
                showNoFilesLayoutIfNeed()
            }
        }
        model.scannerStatusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> TODO()
                is Result.Progress -> showProgress()
                is Result.Success -> hideProgress()
            }
        }

        model.selectedModeTrackLiveData.observe(viewLifecycleOwner) {
            setHasOptionsMenu(it)
            mainSharedViewModel.enabledBackPressedLiveData.value = it
        }

        model.updateTrackLiveData.observe(viewLifecycleOwner) {
            adapter.updateItem(it)
        }

        model.selectedTrackListLiveData.observe(viewLifecycleOwner) { }

        model.removeTrackLiveData.observe(viewLifecycleOwner) {
            adapter.removeItem(it)
        }

        mainSharedViewModel.backKeyPressedLiveData.observe(viewLifecycleOwner) {
            adapter.deselectAllItems()
            model.selectedModeTrackLiveData.value = false
        }
    }

    private fun changePlayingStatusIfNeed(data: Track) {
        trackViewModel.nowPlayingLiveData.value?.let { playingMetadata ->
            if (data.id.toString() == playingMetadata.id) {
                data.playingStatus = playingMetadata.isPlaying
            }
        }
    }

    private fun showNoFilesLayoutIfNeed() {
        noFilesFoundLayout.isVisible = adapter.itemCount == 0
    }

    private fun initPlayingLayout(view: View) {
        initPlayingLayoutViews(view)
        initPlayingLayoutListeners()

        trackViewModel.mediaMetadata.observe(viewLifecycleOwner) { updatePlayingLayout(it) }
        trackViewModel.mediaButtonRes.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it.second)
                .into(playImageView)
            if (it.first) {
                coverImageView.startAnimation(rotateAnimation)
            } else {
                rotateAnimation.cancel()
                rotateAnimation.reset()
            }
        }
        trackViewModel.nowPlayingLiveData.observe(viewLifecycleOwner) {
            updateTrackStateIfNeed(it)
        }
    }

    private fun updateTrackStateIfNeed(metadata: PlayingMetadata) {
        adapter.setPlayingStatus(
            Track(
                metadata.id.toLong(),
                playingStatus = metadata.isPlaying
            )
        )
    }

    private fun initPlayingLayoutViews(view: View) {
        playingLayout = view.findView(R.id.playingLayout)
        playingLayout.isVisible = false
        coverImageView = view.findView(R.id.coverImageView)
        nameTextView = view.findView(R.id.nameTextView)
        singerTextView = view.findView(R.id.singerTextView)
        playImageView = view.findView(R.id.playImageView)
        nextImageView = view.findView(R.id.nextImageView)
    }

    private fun initPlayingLayoutListeners() {
        playImageView.setOnClickListener {
            trackViewModel.mediaMetadata.value?.let { trackViewModel.playMediaId(it.id) }
        }
        nextImageView.setOnClickListener { trackViewModel.nextTrack() }

        playingLayout.setOnClickListener {
            trackViewModel.mediaMetadata.value?.let {
                model.navigateToTrackScreen(Track(it.id.toLong()))
            }
        }
        playingLayout.setOnLongClickListener {
            trackViewModel.mediaMetadata.value?.let {
                val position = adapter.getItemIndexById(it.id.toLong())
                trackRecyclerView.smoothScrollToPosition(position)
            }
            true
        }
    }

    private fun updatePlayingLayout(metadata: PlayingMetadata) {
        playingLayout.isVisible = true
        nameTextView.text = metadata.title
        nameTextView.isFocusableInTouchMode = true
        nameTextView.requestFocus()
        nameTextView.marqueeRepeatLimit = -1
        singerTextView.text = metadata.subtitle
    }

    private fun hideProgress() {
        progressLayout.visibility = View.INVISIBLE
        trackRecyclerView.visibility = View.VISIBLE
    }

    private fun showProgress() {
        progressLayout.visibility = View.VISIBLE
        trackRecyclerView.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.track_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.deleteMenuItem -> {
            ConfirmOperationDialog(
                getString(R.string.track_list_delete_track_title),
                getString(R.string.track_list_delete_track_msg),
                model::deleteSelectedTracks
            ).show(parentFragmentManager)
            true
        }
        R.id.informationMenuItem -> {
            model.navigateToTrackInformationScreen()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun getScreenName() = "TrackListScreen"
}