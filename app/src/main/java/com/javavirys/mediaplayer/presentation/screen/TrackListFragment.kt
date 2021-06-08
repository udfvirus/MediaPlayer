package com.javavirys.mediaplayer.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.javavirys.mediaplayer.R
import com.javavirys.mediaplayer.di.viewmodel.TrackListViewModelFactory
import com.javavirys.mediaplayer.presentation.adapter.TrackAdapter
import com.javavirys.mediaplayer.presentation.viewmodel.TrackListViewModel
import com.javavirys.mediaplayer.util.extension.findView


class TrackListFragment : Fragment(R.layout.fragment_track_list) {

    private val model: TrackListViewModel by viewModels { TrackListViewModelFactory() }

    private val adapter by lazy { TrackAdapter(model::navigateToTrackScreen) }

    private lateinit var trackRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        model.loadTracks()
            .observe(viewLifecycleOwner) {
                adapter.items = it
                adapter.notifyDataSetChanged()
            }
    }
}