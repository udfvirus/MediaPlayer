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
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.core.entity.Result
import com.javavirys.mediaplayer.presentation.adapter.TrackAdapter
import com.javavirys.mediaplayer.presentation.viewmodel.TrackListViewModel
import com.javavirys.mediaplayer.util.extension.findView
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackListFragment : BaseFragment<TrackListViewModel>(R.layout.fragment_track_list) {

    override val model: TrackListViewModel by viewModel()

    private val adapter by lazy {
        TrackAdapter {
            model.navigateToTrackScreen(it)
        }
    }

    private lateinit var trackRecyclerView: RecyclerView

    private lateinit var progressLayout: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressLayout = view.findView(R.id.progressLayout)

        initToolbar()
        initRecyclerView(view)
    }

    private fun initToolbar() {
        val toolbar = requireActivity().findView<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.track_list_title)
    }

    private fun initRecyclerView(view: View) {
        trackRecyclerView = view.findView(R.id.trackRecyclerView)
        trackRecyclerView.adapter = adapter

        model.tracksLiveData.observe(viewLifecycleOwner) {
            if (it is Result.Success) {
                adapter.addItem(it.data)
            }
        }
        model.scannerStatusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> TODO()
                is Result.Progress -> showProgress()
                is Result.Success -> hideProgress()
            }
        }
    }

    private fun hideProgress() {
        progressLayout.visibility = View.INVISIBLE
        trackRecyclerView.visibility = View.VISIBLE
    }

    private fun showProgress() {
        progressLayout.visibility = View.VISIBLE
        trackRecyclerView.visibility = View.INVISIBLE
    }
}